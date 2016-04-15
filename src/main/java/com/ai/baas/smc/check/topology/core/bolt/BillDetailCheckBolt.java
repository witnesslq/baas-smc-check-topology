package com.ai.baas.smc.check.topology.core.bolt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.ai.baas.dshm.client.CacheFactoryUtil;
import com.ai.baas.dshm.client.impl.CacheBLMapper;
import com.ai.baas.dshm.client.impl.DshmClient;
import com.ai.baas.dshm.client.interfaces.IDshmClient;
import com.ai.baas.smc.check.topology.DAO.StlBillDataDAO;
import com.ai.baas.smc.check.topology.DAO.StlBillItemDataDAO;
import com.ai.baas.smc.check.topology.constants.SmcCacheConstant;
import com.ai.baas.smc.check.topology.constants.SmcCacheConstant.Dshm.FieldName;
import com.ai.baas.smc.check.topology.constants.SmcCacheConstant.ParamCode;
import com.ai.baas.smc.check.topology.constants.SmcCacheConstant.TypeCode;
import com.ai.baas.smc.check.topology.constants.SmcConstant;
import com.ai.baas.smc.check.topology.constants.SmcExceptCodeConstant;
import com.ai.baas.smc.check.topology.constants.SmcHbaseConstant;
import com.ai.baas.smc.check.topology.vo.StlBillData;
import com.ai.baas.smc.check.topology.vo.StlBillItemData;
import com.ai.baas.smc.check.topology.vo.StlBillStyleItem;
import com.ai.baas.smc.check.topology.vo.StlPolicy;
import com.ai.baas.smc.check.topology.vo.StlSysParam;
import com.ai.baas.storm.jdbc.JdbcProxy;
import com.ai.baas.storm.message.MappingRule;
import com.ai.baas.storm.message.MessageParser;
import com.ai.baas.storm.util.BaseConstants;
import com.ai.baas.storm.util.HBaseProxy;
import com.ai.opt.base.exception.BusinessException;
import com.ai.opt.base.exception.SystemException;
import com.ai.opt.sdk.cache.factory.CacheClientFactory;
import com.ai.opt.sdk.util.CollectionUtil;
import com.ai.opt.sdk.util.StringUtil;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * 对账<br>
 * Date: 2016年3月25日 <br>
 * Copyright (c) 2016 asiainfo.com <br>
 * 
 * @author mayt
 */
public class BillDetailCheckBolt extends BaseBasicBolt {
    private static final Logger LOG = LoggerFactory.getLogger(BillDetailCheckBolt.class);

    private static final long serialVersionUID = -3214008757998306486L;

    private ICacheClient policyCacheClient;

    private ICacheClient billStyleCacheClient;

    private ICacheClient calParamCacheClient;

    private ICacheClient countCacheClient;

    private ICacheClient sysParamCacheClient;

    private IDshmClient dshmClient;

    private String[] outputFields = new String[] { "data" };

    private MappingRule[] mappingRules = new MappingRule[2];

    private StlBillDataDAO stlBillDataDAO;

    private StlBillItemDataDAO stlBillItemDataDAO;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
        if (policyCacheClient == null) {
            policyCacheClient = CacheClientFactory
                    .getCacheClient(SmcCacheConstant.NameSpace.POLICY_CACHE);
        }
        if (billStyleCacheClient == null) {
            billStyleCacheClient = CacheClientFactory
                    .getCacheClient(SmcCacheConstant.NameSpace.BILL_STYLE_CACHE);
        }
        if (calParamCacheClient == null) {
            calParamCacheClient = CacheFactoryUtil.getCacheClient(CacheBLMapper.CACHE_BL_CAL_PARAM);
        }
        if (countCacheClient == null) {
            countCacheClient = CacheClientFactory
                    .getCacheClient(SmcCacheConstant.NameSpace.CHECK_COUNT_CACHE);
        }
        if (sysParamCacheClient == null) {
            sysParamCacheClient = CacheClientFactory
                    .getCacheClient(SmcCacheConstant.NameSpace.SYS_PARAM_CACHE);
        }
        if (dshmClient == null) {
            dshmClient = new DshmClient();
        }
        mappingRules[0] = MappingRule.getMappingRule(MappingRule.FORMAT_TYPE_OUTPUT,
                BaseConstants.JDBC_DEFAULT);
        mappingRules[1] = mappingRules[0];
        /* 3.初始化hbase */
        HBaseProxy.loadResource(stormConf);
        if (stlBillDataDAO == null) {
            stlBillDataDAO = new StlBillDataDAO();
        }
        if (stlBillItemDataDAO == null) {
            stlBillItemDataDAO = new StlBillItemDataDAO();
        }

    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        try {
            String inputData = input.getString(0);
            /* 1.获取并解析输入信息 */
            MessageParser messageParser = MessageParser.parseObject(inputData, mappingRules,
                    outputFields);
            Map<String, String> data = messageParser.getData();
            String tenantId = data.get(SmcConstant.FmtFeildName.TENANT_ID);
            String batchNo = data.get(SmcConstant.FmtFeildName.BATCH_NO);
            String totalRecord = data.get(SmcConstant.FmtFeildName.TOTAL_RECORD);
            String orderId = data.get(SmcConstant.FmtFeildName.ORDER_ID);
            String feeItemId3pl = data.get(SmcConstant.FmtFeildName.FEE_ITEM_ID);
            String itemFee3pl = data.get(SmcConstant.FmtFeildName.TOTAL_FEE);
            // 查询导入日志
            Map<String, String> params = new TreeMap<String, String>();
            params.put(SmcCacheConstant.Dshm.FieldName.TENANT_ID, tenantId);
            params.put(SmcCacheConstant.Dshm.FieldName.BATCH_NO, batchNo);
            List<Map<String, String>> results = dshmClient
                    .list(SmcCacheConstant.Dshm.TableName.STL_IMPORT_LOG).where(params)
                    .executeQuery(calParamCacheClient);
            if (CollectionUtil.isEmpty(results)) {
                throw new BusinessException(SmcExceptCodeConstant.BUSINESS_EXCEPTION, "导入日志不存在");
            }
            Map<String, String> importLogMap = results.get(0);// 导入日志map
            String billTimeSn = importLogMap.get(FieldName.BILL_TIME_SN);// 账期
            String yyyyMm = StringUtil.restrictLength(billTimeSn, 6);
            String objectId = importLogMap.get(FieldName.OBJECT_ID);// 数据对象
            // 查询第三方账单
            StlBillData stlBillDataQuery = new StlBillData();
            stlBillDataQuery.setTenantId(tenantId);
            stlBillDataQuery.setBatchNo(batchNo);
            stlBillDataQuery.setBillFrom(SmcConstant.StlBillData.BillFrom.IMPORT);
            stlBillDataQuery.setBillTimeSn(billTimeSn);
            List<StlBillData> stlBillDatas = stlBillDataDAO.query(
                    JdbcProxy.getConnection(BaseConstants.JDBC_DEFAULT), yyyyMm, stlBillDataQuery);
            StlBillData billData3pl = stlBillDatas.get(0);// 第三方账单
            String policyCode = billData3pl.getPolicyCode();// 政策编码
            Long billId3pl = billData3pl.getBillId();// 第三方账单ID
            // 1， 根据第三方账单的租户、政策编码、结算账期、结算方加载本系统结算算费结果帐单数据
            stlBillDataQuery = new StlBillData();
            stlBillDataQuery.setTenantId(tenantId);
            stlBillDataQuery.setPolicyCode(policyCode);
            stlBillDataQuery.setBillTimeSn(billData3pl.getBillTimeSn());
            stlBillDataQuery.setStlElementSn(billData3pl.getStlElementSn());
            stlBillDatas = stlBillDataDAO.query(
                    JdbcProxy.getConnection(BaseConstants.JDBC_DEFAULT), yyyyMm, stlBillDataQuery);
            StlBillData billDataSys = stlBillDatas.get(0);// 本系统账单
            Long billIdSys = billDataSys.getBillId();// 本系统账单ID
            // 查询政策信息
            StringBuilder key = new StringBuilder();
            key.append(tenantId).append(".").append(policyCode);
            String policyStr = policyCacheClient.get(key.toString());
            if (StringUtil.isBlank(policyStr)) {
                throw new BusinessException(SmcExceptCodeConstant.BUSINESS_EXCEPTION, "政策["
                        + policyCode + "]不存在");
            }
            StlPolicy stlPolicy = JSON.parseObject(policyStr, StlPolicy.class);
            // 查询详单项配置
            StringBuilder keyStringBuilder = new StringBuilder();
            keyStringBuilder.append(tenantId).append(SmcCacheConstant.CACHE_KEY_SPLIT)
                    .append(stlPolicy.getBillStyleSn()).append(SmcCacheConstant.CACHE_KEY_SPLIT)
                    .append(SmcCacheConstant.BILL_DETAIL_ITEM);
            String cacheStr = billStyleCacheClient.get(keyStringBuilder.toString());
            if (StringUtil.isBlank(cacheStr)) {
                throw new SystemException("账单样式编码[" + stlPolicy.getBillStyleSn() + "]详单项配置不存在");
            }
            List<StlBillStyleItem> stlBillStyleItems = JSON.parseArray(cacheStr,
                    StlBillStyleItem.class);
            // 3， 根据详单项配置解析本详单数据；(开头已解析)

            // 4， 根据本详单的租户、流水号、政策编码、账期获取本系统结算算费结果详单数据
            // KEY:租户ID_账单ID_账期ID_数据对象_账单来源_流水ID_主键ID
            String rowKey = new StringBuilder().append(tenantId)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(billIdSys)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(billTimeSn)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(objectId)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT)
                    .append(SmcConstant.StlBillData.BillFrom.SYS)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(orderId)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).toString();
            RowFilter rowFilter = new RowFilter(CompareOp.EQUAL, new BinaryPrefixComparator(
                    rowKey.getBytes()));
            Scan scan = new Scan();
            scan.setFilter(rowFilter);
            Table tableBillDetailData = HBaseProxy.getConnection().getTable(
                    TableName.valueOf(SmcHbaseConstant.TableName.STL_BILL_DETAIL_DATA_ + yyyyMm));
            ResultScanner resultScanner = tableBillDetailData.getScanner(scan);
            Result result = resultScanner.next();

            String feeItemIdSys = null;
            String itemFeeSys = "0";

            if (result != null) {
                NavigableMap<byte[], byte[]> billDetailDataSysMap = result
                        .getFamilyMap(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes());
                feeItemIdSys = new String(
                        billDetailDataSysMap.get(SmcHbaseConstant.ColumnName.FEE_ITEM_ID.getBytes()));
                itemFeeSys = new String(
                        billDetailDataSysMap.get(SmcHbaseConstant.ColumnName.ITEM_FEE.getBytes()));

            }
            // 5， 如果不存在此流水或此流水对应的科目金额不一致，
            // 向详单差异表中插入此差异详单表（stl_bill_detail_diff_data_yyyymm）
            if (StringUtil.isBlank(feeItemIdSys) || StringUtil.isBlank(itemFeeSys)
                    || !feeItemId3pl.equals(feeItemIdSys) || !itemFee3pl.equals(itemFeeSys)) {
                String diffFee = itemFee3pl;
                String checkStateDesc = "无匹配记录";
                if (!StringUtil.isBlank(feeItemIdSys) && !StringUtil.isBlank(itemFeeSys)
                        && feeItemId3pl.equals(feeItemIdSys)) {
                    diffFee = String.valueOf(Long.parseLong(itemFee3pl)
                            - Long.parseLong(itemFeeSys));
                    checkStateDesc = "金额不一致";
                }
                // 查询第三方详单
                // KEY:租户ID_账单ID_账期ID_数据对象_账单来源_流水ID_主键ID
                rowKey = new StringBuilder().append(tenantId).append(SmcHbaseConstant.ROWKEY_SPLIT)
                        .append(billId3pl).append(SmcHbaseConstant.ROWKEY_SPLIT).append(billTimeSn)
                        .append(SmcHbaseConstant.ROWKEY_SPLIT).append(objectId)
                        .append(SmcHbaseConstant.ROWKEY_SPLIT)
                        .append(SmcConstant.StlBillData.BillFrom.IMPORT)
                        .append(SmcHbaseConstant.ROWKEY_SPLIT).append(orderId)
                        .append(SmcHbaseConstant.ROWKEY_SPLIT).toString();
                rowFilter = new RowFilter(CompareOp.EQUAL, new BinaryPrefixComparator(
                        rowKey.getBytes()));
                scan = new Scan();
                scan.setFilter(rowFilter);
                resultScanner = tableBillDetailData.getScanner(scan);
                result = resultScanner.next();
                if (result == null) {
                    throw new BusinessException(SmcExceptCodeConstant.BUSINESS_EXCEPTION,
                            "第三方详单数据不存在[rowKey:" + rowKey + "]");
                }
                // 写入差异表
                NavigableMap<byte[], byte[]> map = result
                        .getFamilyMap(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes());
                rowKey = new String(map.get(SmcHbaseConstant.ColumnName.STL_ORDER_DATA_KEY
                        .getBytes()));
                Put put = new Put(rowKey.getBytes());
                while (true) {
                    Entry<byte[], byte[]> entry = map.pollFirstEntry();
                    if (entry == null) {
                        break;
                    }
                    put.addColumn(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes(),
                            entry.getKey(), entry.getValue());
                }
                put.addColumn(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes(),
                        SmcHbaseConstant.ColumnName.DIFF_FEE.getBytes(), diffFee.getBytes());
                put.addColumn(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes(),
                        SmcHbaseConstant.ColumnName.CHECK_STATE.getBytes(),
                        SmcConstant.StlBillDetailDiffData.CheckState.DIFF.getBytes());
                put.addColumn(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes(),
                        SmcHbaseConstant.ColumnName.CHECK_STATE_DESC.getBytes(),
                        checkStateDesc.getBytes());
                Table tableBillDetailDiffData = HBaseProxy.getConnection().getTable(
                        TableName.valueOf(SmcHbaseConstant.TableName.STL_BILL_DETAIL_DIFF_DATA_
                                + yyyyMm));
                tableBillDetailDiffData.put(put);
            }
            // 6， 本账单对账次数加1（redis），
            String countKey = "billdata_" + tenantId + "_" + batchNo + "_records";
            Long countRecord = countCacheClient.incr(countKey);
            // 如果对账次数＝第三方账单详单记录数，则说明第三方详单都已对账完成：
            if (Long.parseLong(totalRecord) != countRecord.longValue()) {
                return;
            }
            // a) 查询本系统结算算费结果详单，查询本系统存在记录，而第三方详单不存在的记录，把
            // 些记录插入差异详单表（stl_bill_detail_diff_data_yyyymm）
            // KEY:租户ID_账单ID_账期ID_数据对象_账单来源_流水ID_主键ID
            key = new StringBuilder().append(tenantId).append(SmcHbaseConstant.ROWKEY_SPLIT)
                    .append(billIdSys).append(SmcHbaseConstant.ROWKEY_SPLIT).append(billTimeSn)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(objectId)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT)
                    .append(SmcConstant.StlBillData.BillFrom.SYS)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT);
            rowFilter = new RowFilter(CompareOp.EQUAL,
                    new BinaryPrefixComparator(rowKey.getBytes()));
            scan = new Scan();
            scan.setFilter(rowFilter);
            resultScanner = tableBillDetailData.getScanner(scan);
            for (Result resultTmp : resultScanner) {
                NavigableMap<byte[], byte[]> map = resultTmp
                        .getFamilyMap(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes());
                String orderIdTmp = new String(map.get(SmcHbaseConstant.ColumnName.ORDER_ID
                        .getBytes()));
                // 查询第三方详单
                rowKey = new StringBuilder().append(tenantId).append(SmcHbaseConstant.ROWKEY_SPLIT)
                        .append(billId3pl).append(SmcHbaseConstant.ROWKEY_SPLIT).append(billTimeSn)
                        .append(SmcHbaseConstant.ROWKEY_SPLIT).append(objectId)
                        .append(SmcHbaseConstant.ROWKEY_SPLIT)
                        .append(SmcConstant.StlBillData.BillFrom.IMPORT)
                        .append(SmcHbaseConstant.ROWKEY_SPLIT).append(orderIdTmp)
                        .append(SmcHbaseConstant.ROWKEY_SPLIT).toString();
                rowFilter = new RowFilter(CompareOp.EQUAL, new BinaryPrefixComparator(
                        rowKey.getBytes()));
                scan = new Scan();
                scan.setFilter(rowFilter);
                ResultScanner resultScannerTmp = tableBillDetailData.getScanner(scan);
                Result result3pl = resultScannerTmp.next();
                if (result3pl == null) {
                    // 插入差异表
                    String itemFeeTmp = new String(map.get(SmcHbaseConstant.ColumnName.ITEM_FEE
                            .getBytes()));
                    rowKey = new String(map.get(SmcHbaseConstant.ColumnName.STL_ORDER_DATA_KEY
                            .getBytes()));
                    Put put = new Put(rowKey.getBytes());
                    while (true) {
                        Entry<byte[], byte[]> entry = map.pollFirstEntry();
                        if (entry == null) {
                            break;
                        }
                        put.addColumn(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes(),
                                entry.getKey(), entry.getValue());
                    }
                    put.addColumn(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes(),
                            SmcHbaseConstant.ColumnName.DIFF_FEE.getBytes(), itemFeeTmp.getBytes());
                    put.addColumn(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes(),
                            SmcHbaseConstant.ColumnName.CHECK_STATE.getBytes(),
                            SmcConstant.StlBillDetailDiffData.CheckState.DIFF.getBytes());
                    put.addColumn(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes(),
                            SmcHbaseConstant.ColumnName.CHECK_STATE_DESC.getBytes(),
                            "记录缺失".getBytes());
                    Table tableBillDetailDiffData = HBaseProxy.getConnection().getTable(
                            TableName.valueOf(SmcHbaseConstant.TableName.STL_BILL_DETAIL_DIFF_DATA_
                                    + yyyyMm));
                    tableBillDetailDiffData.put(put);
                }
            }
            // b) 修改账单数据表（第三方账单和本系统结算算费结果帐单）中的对账结果（差异金额为0则沉淀状态为账单一致，否则沉淀状态为有差异）。
            // 7， 如果对账结果为有差异，则调用对账错误详单文件生成方法，生成错误详单文件，并向账详单处理结果文件清单表新增记录。
            // 生成文件
            createFile(billData3pl, billDataSys, objectId);
            // 8， 完成
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createFile(StlBillData billData3pl, StlBillData billDataSys, String objectId) {
        String tenantId = billData3pl.getTenantId();
        Long billId3pl = billData3pl.getBillId();
        Long billIdSys = billDataSys.getBillId();
        String billTimeSn = billData3pl.getBillTimeSn();
        String yyyyMm = StringUtil.restrictLength(billTimeSn, 6);
        // 1. 根据租户ID、账期月份、账单ID查询账单表及账单科目汇总表，获取账单信息；
        StlBillItemData stlBillItemDataQuery = new StlBillItemData();
        stlBillItemDataQuery.setTenantId(billData3pl.getTenantId());
        stlBillItemDataQuery.setBillId(billData3pl.getBillId());
        List<StlBillItemData> stlBillItemDatas;
        Workbook wb = null;
        try {
            try {
                stlBillItemDatas = stlBillItemDataDAO.query(
                        JdbcProxy.getConnection(BaseConstants.JDBC_DEFAULT),
                        StringUtil.restrictLength(billData3pl.getBillTimeSn(), 6),
                        stlBillItemDataQuery);
            } catch (Exception e) {
                throw new SystemException(e);
            }

            // 2. 根据账单模板生成账单excel文件(文件名：ERR_租户ID_结算方ID 政策编码_账期_账单.xlsx)；

            wb = new XSSFWorkbook();
            XSSFSheet sheet0 = (XSSFSheet) wb.createSheet("账单");
            XSSFRow row0 = sheet0.createRow(0);// 第一行
            XSSFCell cell = row0.createCell(0);
            cell.setCellValue("结算方");
            cell = row0.createCell(1);
            cell.setCellValue(billData3pl.getStlElementSn());
            cell = row0.createCell(2);
            cell.setCellValue("批次号");
            cell = row0.createCell(3);
            cell.setCellValue(billData3pl.getBatchNo());

            XSSFRow row1 = sheet0.createRow(1);// 第二行
            cell = row1.createCell(0);
            cell.setCellValue("政策编码");
            cell = row1.createCell(1);
            cell.setCellValue(billData3pl.getPolicyCode());
            cell = row1.createCell(2);
            cell.setCellValue("账期");
            cell = row1.createCell(3);
            cell.setCellValue(billData3pl.getBillTimeSn());

            XSSFRow row2 = sheet0.createRow(2);// 第三行
            cell = row2.createCell(0);
            cell.setCellValue("开始时间");
            cell = row2.createCell(1);
            cell.setCellValue(billData3pl.getBillStartTime());
            cell = row2.createCell(2);
            cell.setCellValue("结束时间");
            cell = row2.createCell(3);
            cell.setCellValue(billData3pl.getBillEndTime());

            XSSFRow row3 = sheet0.createRow(3);// 第四行
            cell = row3.createCell(0);
            cell.setCellValue("结算金额(元)");
            cell = row3.createCell(1);
            cell.setCellValue(billData3pl.getOrigFee());
            cell = row3.createCell(2);
            cell.setCellValue("差异金额(元)");
            cell = row3.createCell(3);
            cell.setCellValue(billData3pl.getDiffFee());

            XSSFRow row5 = sheet0.createRow(5);// 第六行
            cell = row5.createCell(0);
            cell.setCellValue("科目ID");
            cell = row5.createCell(1);
            cell.setCellValue("科目名称");
            cell = row5.createCell(2);
            cell.setCellValue("总金额(元)");
            cell = row5.createCell(3);
            cell.setCellValue("差异金额(元)");

            int i = 6;
            for (StlBillItemData stlBillItemData : stlBillItemDatas) {
                XSSFRow rowTmp = sheet0.createRow(i);
                cell = rowTmp.createCell(0);
                cell.setCellValue(stlBillItemData.getFeeItemId());
                cell = rowTmp.createCell(1);
                cell.setCellValue(getSysParamDesc(stlBillItemData.getTenantId(),
                        TypeCode.STL_POLICY_ITEM_PLAN, ParamCode.FEE_ITEM,
                        stlBillItemData.getFeeItemId()));
                cell = rowTmp.createCell(2);
                cell.setCellValue(stlBillItemData.getTotalFee());
                cell = rowTmp.createCell(3);
                cell.setCellValue(stlBillItemData.getDiffFee());
            }

            String excelFileName = "ERR_" + billData3pl.getTenantId() + "_"
                    + billData3pl.getStlElementSn() + "_" + billData3pl.getPolicyCode() + "_"
                    + billData3pl.getBillTimeSn() + "_BILL.xlsx";
            String tmpPath = System.getProperty("user.dir") + "/tmp/";

            File file = new File(tmpPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = null;
            fileOut = new FileOutputStream(tmpPath + excelFileName);
            wb.write(fileOut);
            // 3. 生成详单文件（文件名：ERR_租户ID_结算方ID 政策编码_账期_详单_序号.cvs)）

            // 取差异详单
            // KEY:租户ID_账单ID_账期ID_数据对象_账单来源_流水ID_主键ID
            // 第三方差异详单
            String rowKey = new StringBuilder().append(tenantId)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(billId3pl)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(billTimeSn)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(objectId)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT)
                    .append(SmcConstant.StlBillData.BillFrom.IMPORT)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).toString();

            RowFilter rowFilter = new RowFilter(CompareOp.EQUAL, new BinaryPrefixComparator(
                    rowKey.getBytes()));
            Scan scan = new Scan();
            scan.setFilter(rowFilter);

            Table tableBillDetailDiffData = HBaseProxy.getConnection().getTable(
                    TableName.valueOf(SmcHbaseConstant.TableName.STL_BILL_DETAIL_DIFF_DATA_
                            + yyyyMm));
            ResultScanner resultScanner3pl = tableBillDetailDiffData.getScanner(scan);

            // 本系统差异详单
            rowKey = new StringBuilder().append(tenantId).append(SmcHbaseConstant.ROWKEY_SPLIT)
                    .append(billIdSys).append(SmcHbaseConstant.ROWKEY_SPLIT).append(billTimeSn)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(objectId)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT)
                    .append(SmcConstant.StlBillData.BillFrom.SYS)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).toString();

            rowFilter = new RowFilter(CompareOp.EQUAL,
                    new BinaryPrefixComparator(rowKey.getBytes()));
            scan = new Scan();
            scan.setFilter(rowFilter);

            ResultScanner resultScannerSys = tableBillDetailDiffData.getScanner(scan);

            int sort = 0;
            boolean has3pl = true;
            boolean hasSys = true;
            int count = 0;// 当前文件条数
            while (true) {
                Result rt;
                if (has3pl) {
                    rt = resultScanner3pl.next();
                    if (rt == null) {
                        has3pl = false;
                        rt = resultScannerSys.next();
                    }
                } else {
                    rt = resultScannerSys.next();
                }
                if (rt == null) {
                    break;
                }
                NavigableMap<byte[], byte[]> map = rt
                        .getFamilyMap(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes());

                i++;
                String cvsFileName = "ERR_" + billData3pl.getTenantId() + "_"
                        + billData3pl.getStlElementSn() + "_" + billData3pl.getPolicyCode() + "_"
                        + billData3pl.getBillTimeSn() + "_BILL_DETAIL_" + sort + ".cvs";

                File csvFile = null;
                BufferedWriter writer = null;
                csvFile = new File(tmpPath + cvsFileName);
                File parent = csvFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                csvFile.createNewFile();
                // GB2312使正确读取分隔符","
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile),
                        "GB2312"), 1024);
                // 写入文件头部
                writer.write("a,b");
                writer.newLine();
                writer.write(i + "a," + i + "b");
                writer.flush();
                writer.close();
            }

            // a) 第一行为批次号（账单ID）＋差异详单总记录数
            // b)
            // 第三行开始为差异详单明细，数据来源根据租户ID、账单ID查询详单差异数据表，获取差异详单清单，
            // 再根据差异详单的KEY从详单数据表中获取具体详单信息，格式及次序根据详单项定义表中的详单项。
            // c) 单个详单文件记录长度为5万条记录，如果记录数超出则拆多个文件方式处理
            // 4. 把账单文件和详单文件文件压缩为一个文件（文件名：ERR_租户ID_结算方ID _政策编码_账期_YYYYMMDDHHMISS.zip）
        } catch (IOException e) {
            throw new SystemException(e);
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                LOG.error("WorkBook关闭失败", e);
            }
        }
    }

    List<StlSysParam> getSysParams(String tenantId, String typeCode, String paramCode) {
        ICacheClient cacheClient = CacheClientFactory
                .getCacheClient(SmcCacheConstant.NameSpace.SYS_PARAM_CACHE);
        StringBuilder key = new StringBuilder();
        key.append(tenantId);
        key.append(".");
        key.append(typeCode);
        key.append(".");
        key.append(paramCode);
        String data = cacheClient.get(key.toString());
        if (StringUtil.isBlank(data)) {
            return null;
        }
        return JSONArray.parseArray(data, StlSysParam.class);
    }

    StlSysParam getSysParam(String tenantId, String typeCode, String paramCode, String columnValue) {
        ICacheClient cacheClient = CacheClientFactory
                .getCacheClient(SmcCacheConstant.NameSpace.SYS_PARAM_CACHE);
        StringBuilder key = new StringBuilder();
        key.append(tenantId);
        key.append(".");
        key.append(typeCode);
        key.append(".");
        key.append(paramCode);
        key.append(".");
        key.append(columnValue);
        String data = cacheClient.get(key.toString());
        if (StringUtil.isBlank(data)) {
            return null;
        }
        return JSONArray.parseObject(data, StlSysParam.class);
    }

    String getSysParamDesc(String tenantId, String typeCode, String paramCode, String columnValue) {
        StlSysParam sysParam = getSysParam(tenantId, typeCode, paramCode, columnValue);
        return sysParam == null ? "" : sysParam.getColumnDesc();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

}
