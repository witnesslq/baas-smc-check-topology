package com.ai.baas.smc.check.topology.core.bolt;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import com.ai.baas.dshm.client.CacheFactoryUtil;
import com.ai.baas.dshm.client.impl.CacheBLMapper;
import com.ai.baas.dshm.client.impl.DshmClient;
import com.ai.baas.dshm.client.interfaces.IDshmClient;
import com.ai.baas.smc.check.topology.DAO.StlBillDataDAO;
import com.ai.baas.smc.check.topology.DAO.StlBillItemDataDAO;
import com.ai.baas.smc.check.topology.DAO.StlImportLogDAO;
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
import com.ai.baas.smc.check.topology.vo.StlImportLog;
import com.ai.baas.smc.check.topology.vo.StlPolicy;
import com.ai.baas.smc.check.topology.vo.StlSysParam;
import com.ai.baas.storm.failbill.FailBillHandler;
import com.ai.baas.storm.jdbc.JdbcProxy;
import com.ai.baas.storm.message.MappingRule;
import com.ai.baas.storm.message.MessageParser;
import com.ai.baas.storm.util.BaseConstants;
import com.ai.baas.storm.util.HBaseProxy;
import com.ai.opt.base.exception.BusinessException;
import com.ai.opt.base.exception.SystemException;
import com.ai.opt.sdk.cache.factory.CacheClientFactory;
import com.ai.opt.sdk.util.CollectionUtil;
import com.ai.opt.sdk.util.DateUtil;
import com.ai.opt.sdk.util.StringUtil;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

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

    private StlImportLogDAO importLogDAO;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
        JdbcProxy.loadResources(Arrays.asList(BaseConstants.JDBC_DEFAULT), stormConf);
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
        if (importLogDAO == null) {
            importLogDAO = new StlImportLogDAO();
        }
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Map<String, String> data = null;
        try {
            String inputData = input.getString(0);
            LOG.info(" ====== 开始执行对账bolt，inputData = [" + inputData + "]");
            /* 1.获取并解析输入信息 */
            MessageParser messageParser = MessageParser.parseObject(inputData, mappingRules,
                    outputFields);
            data = messageParser.getData();
            String tenantId = data.get(SmcHbaseConstant.ColumnName.TENANT_ID);
            String batchNo = data.get(SmcHbaseConstant.ColumnName.BATCH_NO);
            String totalRecord = data.get(SmcHbaseConstant.ColumnName.TOTAL_RECORD);
            String orderId = data.get(SmcHbaseConstant.ColumnName.ORDER_ID);
            String feeItemId3pl = data.get(SmcHbaseConstant.ColumnName.FEE_ITEM_ID);
            String itemFee3pl = data.get(SmcHbaseConstant.ColumnName.ITEM_FEE);
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
            LOG.error(" ====== billTimeSn = " + billTimeSn);
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
            LOG.info("第三方账单ID billId3pl = " + billId3pl);
            // 1， 根据第三方账单的租户、政策编码、结算账期、结算方加载本系统结算算费结果帐单数据
            stlBillDataQuery = new StlBillData();
            stlBillDataQuery.setTenantId(tenantId);
            stlBillDataQuery.setPolicyCode(policyCode);
            stlBillDataQuery.setBillTimeSn(billData3pl.getBillTimeSn());
            stlBillDataQuery.setStlElementSn(billData3pl.getStlElementSn());
            stlBillDataQuery.setBillFrom(SmcConstant.StlBillData.BillFrom.SYS);
            stlBillDatas = stlBillDataDAO.query(
                    JdbcProxy.getConnection(BaseConstants.JDBC_DEFAULT), yyyyMm, stlBillDataQuery);
            StlBillData billDataSys = stlBillDatas.get(0);// 本系统账单
            Long billIdSys = billDataSys.getBillId();// 本系统账单ID
            LOG.info("本系统账单ID billIdSys = " + billIdSys);
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
            // KEY:租户ID_账单ID_账期ID_数据对象_账单来源_流水ID
            String rowKey = new StringBuilder().append(tenantId)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(billIdSys)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(billTimeSn)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(objectId)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT)
                    .append(SmcConstant.StlBillData.BillFrom.SYS)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(orderId).toString();
            RowFilter rowFilter = new RowFilter(CompareOp.EQUAL, new BinaryComparator(
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
                // KEY:租户ID_账单ID_账期ID_数据对象_账单来源_流水ID
                rowKey = new StringBuilder().append(tenantId).append(SmcHbaseConstant.ROWKEY_SPLIT)
                        .append(billId3pl).append(SmcHbaseConstant.ROWKEY_SPLIT).append(billTimeSn)
                        .append(SmcHbaseConstant.ROWKEY_SPLIT).append(objectId)
                        .append(SmcHbaseConstant.ROWKEY_SPLIT)
                        .append(SmcConstant.StlBillData.BillFrom.IMPORT)
                        .append(SmcHbaseConstant.ROWKEY_SPLIT).append(orderId).toString();
                rowFilter = new RowFilter(CompareOp.EQUAL, new BinaryComparator(rowKey.getBytes()));
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
            // Long countRecord = 10l;
            Long countRecord = countCacheClient.incr(countKey);
            LOG.info("对账次数累加器key = " + countKey);
            LOG.info("对账次数累加器value = " + countRecord);
            // 如果对账次数＝第三方账单详单记录数，则说明第三方详单都已对账完成：
            if (Long.parseLong(totalRecord) != countRecord.longValue()) {
                return;
            }
            // a) 查询本系统结算算费结果详单，查询本系统存在记录，而第三方详单不存在的记录，把
            // 些记录插入差异详单表（stl_bill_detail_diff_data_yyyymm）
            // KEY:租户ID_账单ID_账期ID_数据对象_账单来源_流水ID
            key = new StringBuilder().append(tenantId).append(SmcHbaseConstant.ROWKEY_SPLIT)
                    .append(billIdSys).append(SmcHbaseConstant.ROWKEY_SPLIT).append(billTimeSn)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT).append(objectId)
                    .append(SmcHbaseConstant.ROWKEY_SPLIT)
                    .append(SmcConstant.StlBillData.BillFrom.SYS);
            rowFilter = new RowFilter(CompareOp.EQUAL, new BinaryPrefixComparator(key.toString()
                    .getBytes()));
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
                        .append(SmcHbaseConstant.ROWKEY_SPLIT).append(orderIdTmp).toString();
                rowFilter = new RowFilter(CompareOp.EQUAL, new BinaryComparator(rowKey.getBytes()));
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
            createFile(billData3pl, billDataSys, objectId, batchNo, stlBillStyleItems,
                    importLogMap, totalRecord);
            // 8， 完成
        } catch (BusinessException e) {
            LOG.error("详单对账bolt出现异常", e);
            FailBillHandler.addFailBillMsg(data, SmcConstant.BILL_DETAIL_CHECK_BOLT,
                    e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOG.error("详单对账bolt出现异常", e);
            FailBillHandler.addFailBillMsg(data, SmcConstant.BILL_DETAIL_CHECK_BOLT,
                    SmcExceptCodeConstant.SYSTEM_EXCEPTION, e.getMessage());
        } finally {

        }

    }

    private void createFile(StlBillData billData3pl, StlBillData billDataSys, String objectId,
            String batchNo, List<StlBillStyleItem> stlBillStyleItems,
            Map<String, String> importLogMap, String totalRecordDetail) {
        String tenantId = billData3pl.getTenantId();
        Long billId3pl = billData3pl.getBillId();
        Long billIdSys = billDataSys.getBillId();
        String billTimeSn = billData3pl.getBillTimeSn();
        String yyyyMm = StringUtil.restrictLength(billTimeSn, 6);
        int totalRecord = 0;
        // 1. 根据租户ID、账期月份、账单ID查询账单表及账单科目汇总表，获取账单信息；
        StlBillItemData stlBillItemDataQuery = new StlBillItemData();
        stlBillItemDataQuery.setTenantId(tenantId);
        stlBillItemDataQuery.setBillId(billId3pl);
        List<StlBillItemData> stlBillItemDatas;
        try {
            try {
                stlBillItemDatas = stlBillItemDataDAO.query(
                        JdbcProxy.getConnection(BaseConstants.JDBC_DEFAULT), yyyyMm,
                        stlBillItemDataQuery);
            } catch (Exception e) {
                throw new SystemException(e);
            }

            // 2. 根据账单模板生成账单excel文件(文件名：ERR_租户ID_结算方ID 政策编码_账期_账单.xlsx)；
            LOG.info("开始生成账单文件...");
            Workbook wb = new XSSFWorkbook();

            XSSFCellStyle cellStyle = (XSSFCellStyle) wb.createCellStyle();
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

            XSSFSheet sheet0 = (XSSFSheet) wb.createSheet("账单");
            XSSFRow row0 = sheet0.createRow(0);// 第一行
            XSSFCell cell = row0.createCell(0);
            cell.setCellValue("结算方");
            cell.setCellStyle(cellStyle);
            cell = row0.createCell(1);
            cell.setCellValue(billData3pl.getStlElementSn());
            cell = row0.createCell(2);
            cell.setCellValue("批次号");
            cell.setCellStyle(cellStyle);
            cell = row0.createCell(3);
            cell.setCellValue(billData3pl.getBatchNo());

            XSSFRow row1 = sheet0.createRow(1);// 第二行
            cell = row1.createCell(0);
            cell.setCellValue("政策编码");
            cell.setCellStyle(cellStyle);
            cell = row1.createCell(1);
            cell.setCellValue(billData3pl.getPolicyCode());
            cell = row1.createCell(2);
            cell.setCellValue("账期");
            cell.setCellStyle(cellStyle);
            cell = row1.createCell(3);
            cell.setCellValue(billData3pl.getBillTimeSn());

            XSSFRow row2 = sheet0.createRow(2);// 第三行
            cell = row2.createCell(0);
            cell.setCellValue("开始时间");
            cell.setCellStyle(cellStyle);
            cell = row2.createCell(1);
            cell.setCellValue(DateUtil.getDateString(billData3pl.getBillStartTime(),
                    DateUtil.DATE_FORMAT));
            cell = row2.createCell(2);
            cell.setCellValue("结束时间");
            cell.setCellStyle(cellStyle);
            cell = row2.createCell(3);
            cell.setCellValue(DateUtil.getDateString(billData3pl.getBillEndTime(),
                    DateUtil.DATE_FORMAT));

            XSSFRow row3 = sheet0.createRow(3);// 第四行
            cell = row3.createCell(0);
            cell.setCellValue("结算金额(元)");
            cell.setCellStyle(cellStyle);
            cell = row3.createCell(1);
            cell.setCellValue(billData3pl.getOrigFee() / 1000);
            cell = row3.createCell(2);
            cell.setCellValue("差异金额(元)");
            cell.setCellStyle(cellStyle);
            cell = row3.createCell(3);
            cell.setCellValue(billData3pl.getDiffFee() / 1000);

            XSSFRow row5 = sheet0.createRow(5);// 第六行
            cell = row5.createCell(0);
            cell.setCellValue("科目ID");
            cell.setCellStyle(cellStyle);
            cell = row5.createCell(1);
            cell.setCellValue("科目名称");
            cell.setCellStyle(cellStyle);
            cell = row5.createCell(2);
            cell.setCellValue("总金额(元)");
            cell.setCellStyle(cellStyle);
            cell = row5.createCell(3);
            cell.setCellValue("差异金额(元)");
            cell.setCellStyle(cellStyle);

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
                cell.setCellValue(stlBillItemData.getTotalFee() / 1000);
                cell = rowTmp.createCell(3);
                cell.setCellValue(stlBillItemData.getDiffFee() / 1000);
                i++;
            }

            String excelFileName = "ERR_" + billData3pl.getTenantId() + "_"
                    + billData3pl.getStlElementSn() + "_" + billData3pl.getPolicyCode() + "_"
                    + billData3pl.getBillTimeSn() + "_BILL.xlsx";
            String tmpPath = System.getProperty("user.dir") + "/tmp/" + billData3pl.getTenantId()
                    + billData3pl.getBillTimeSn() + "/"
                    + DateUtil.getDateString(DateUtil.YYYYMMDDHHMMSS);
            LOG.info("excelFileName = " + excelFileName);

            File file = new File(tmpPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOut = null;
            fileOut = new FileOutputStream(tmpPath + "/" + excelFileName);
            wb.write(fileOut);
            // 3. 生成详单文件（文件名：ERR_租户ID_结算方ID 政策编码_账期_详单_序号.cvs)）
            LOG.info("开始生成详单文件...");
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
            for (Result result : resultScanner3pl) {
                totalRecord++;
            }
            resultScanner3pl = tableBillDetailDiffData.getScanner(scan);
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
            for (Result result : resultScannerSys) {
                totalRecord++;
            }
            resultScannerSys = tableBillDetailDiffData.getScanner(scan);

            int sort = 0;
            boolean has3pl = true;
            boolean hasSys = true;

            while (has3pl || hasSys) {
                i++;
                String cvsFileName = "ERR_" + billData3pl.getTenantId() + "_"
                        + billData3pl.getStlElementSn() + "_" + billData3pl.getPolicyCode() + "_"
                        + billData3pl.getBillTimeSn() + "_BILL_DETAIL_" + sort + ".csv";
                LOG.info("cvsFileName = " + cvsFileName);
                File csvFile = null;
                BufferedWriter writer = null;
                csvFile = new File(tmpPath + "/" + cvsFileName);
                File parent = csvFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                csvFile.createNewFile();
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile),
                        SmcConstant.CHARSET_GBK));
                // 写入文件头部
                writer.write("批次号");
                writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
                writer.write(batchNo);
                writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
                writer.write("总数量");
                writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
                writer.write(String.valueOf(totalRecord));
                writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
                writer.write("本文件记录数");
                writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
                if (i <= totalRecord / 50000) {
                    writer.write("50000");
                } else {
                    writer.write(String.valueOf(totalRecord % 50000));
                }
                writer.newLine();// 第二行
                for (StlBillStyleItem billStyleItem : stlBillStyleItems) {
                    writer.write(billStyleItem.getItemTitle());
                    writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
                }
                writer.write("对账结果");
                writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
                writer.write("差异金额(元)");
                writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
                writer.write("差异说明");

                int count = 0;// 当前文件条数
                while (count < 50001) {
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
                        hasSys = false;
                        break;
                    }
                    writer.newLine();
                    NavigableMap<byte[], byte[]> map = rt
                            .getFamilyMap(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes());
                    for (StlBillStyleItem billStyleItem : stlBillStyleItems) {
                        String value = "";
                        if (SmcHbaseConstant.ColumnName.ITEM_FEE
                                .equals(billStyleItem.getItemCode())) {
                            value = String.valueOf(Float.parseFloat(new String(map
                                    .get(billStyleItem.getItemCode().getBytes()))) / 1000);
                        } else {
                            value = new String(map.get(billStyleItem.getItemCode().getBytes()));
                        }
                        writer.write(value);
                        writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
                    }
                    writer.write(new String(map.get(SmcHbaseConstant.ColumnName.CHECK_STATE
                            .getBytes())));
                    writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
                    String diffFee = new String(map.get(SmcHbaseConstant.ColumnName.DIFF_FEE
                            .getBytes()));

                    writer.write(String.valueOf(Float.parseFloat(diffFee) / 1000));
                    writer.write(SmcConstant.CVSFILE_FEILD_SPLIT);
                    writer.write(new String(map.get(SmcHbaseConstant.ColumnName.CHECK_STATE_DESC
                            .getBytes())));
                }
                writer.flush();
                writer.close();
            }
            // 生成zip文件
            // 文件名：ERR_租户ID_结算方ID _政策编码_账期_YYYYMMDDHHMISS.zip
            String targetName = "ERR_" + billData3pl.getTenantId() + "_"
                    + billData3pl.getStlElementSn() + "_" + billData3pl.getPolicyCode() + "_"
                    + billData3pl.getBillTimeSn() + "_"
                    + DateUtil.getDateString(DateUtil.YYYYMMDDHHMMSS) + ".zip";
            String pathRes = tmpPath;
            String targetPath = System.getProperty("user.dir") + "/tmpzip/"
                    + billData3pl.getTenantId() + billData3pl.getBillTimeSn();
            File resourcesFile = new File(pathRes); // 源文件
            File targetFile = new File(targetPath); // 目的
            // 如果目的路径不存在，则新建
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }

            FileOutputStream outputStream = new FileOutputStream(targetPath + "/" + targetName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(outputStream));
            createCompressedFile(out, resourcesFile, "");
            out.close();
            // 上传到ftp
            String url = getSysParams(tenantId, TypeCode.SFTP_CONF, ParamCode.UPLOAD_URL_DIFF_FILE)
                    .get(0).getColumnValue();
            String host = url.split(":")[0];
            int port = 22;
            String username = getSysParams(tenantId, TypeCode.SFTP_CONF, ParamCode.USER_NAME)
                    .get(0).getColumnValue();
            String password = getSysParams(tenantId, TypeCode.SFTP_CONF, ParamCode.PWD).get(0)
                    .getColumnValue();
            ChannelSftp sftp = null;
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + host + " success");
            String directory = url.split(":")[1];
            String uploadFile = targetPath + "/" + targetName;
            try {
                sftp.cd(directory);
            } catch (SftpException sException) {
                if (ChannelSftp.SSH_FX_NO_SUCH_FILE == sException.id) {
                    makeDir(directory, sftp);
                    sftp.cd(directory);
                }
            }

            File fileUploadZip = new File(uploadFile);
            LOG.info("压缩文件开始上传到服务器...");
            sftp.put(new FileInputStream(fileUploadZip), fileUploadZip.getName());
            sftp.disconnect();
            if (session != null) {
                if (session.isConnected()) {
                    session.disconnect();
                }
            }
            if (channel != null) {
                if (channel.isConnected()) {
                    channel.disconnect();
                }
            }
            LOG.info("压缩文件上传成功...");
            // 更新导入日志表
            StlImportLog importLog = new StlImportLog();
            importLog.setTenantId(importLogMap.get(SmcCacheConstant.Dshm.FieldName.TENANT_ID));
            importLog.setLogId(Long.parseLong(importLogMap
                    .get(SmcCacheConstant.Dshm.FieldName.LOG_ID)));
            importLog.setImportRecords(Long.parseLong(totalRecordDetail));
            importLog.setRstFileName(targetName);
            importLog.setRstFileUrl(url);
            importLog.setState(SmcConstant.StlImportLog.State.DATA_SUCCESS);
            importLog.setStateDesc("数据处理完成");
            importLogDAO.update(JdbcProxy.getConnection(BaseConstants.JDBC_DEFAULT), importLog);

            wb.close();
        } catch (IOException e) {
            throw new SystemException(e);
        } catch (SftpException e) {
            throw new SystemException(e);
        } catch (JSchException e) {
            throw new SystemException(e);
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
        }
    }

    private static void makeDir(String directory, ChannelSftp sftp) throws SftpException {
        System.out.println(directory);
        System.out.println(sftp.pwd());
        String parentPath = new File(directory).getParentFile().getPath().replace("\\", "/");
        if (parentPath.equals("/")) {
            sftp.mkdir(directory.substring(1));
        } else {
            try {
                sftp.cd(parentPath);
            } catch (SftpException sException) {
                if (ChannelSftp.SSH_FX_NO_SUCH_FILE == sException.id) {
                    makeDir(parentPath, sftp);
                }
            }
            sftp.mkdir(directory);
        }
    }

    void createCompressedFile(ZipOutputStream out, File file, String dir) {
        try {
            // 如果当前的是文件夹，则进行进一步处理
            if (file.isDirectory()) {
                // 得到文件列表信息
                File[] files = file.listFiles();
                // 将文件夹添加到下一级打包目录
                out.putNextEntry(new ZipEntry(dir + "/"));

                dir = dir.length() == 0 ? "" : dir + "/";

                // 循环将文件夹中的文件打包
                for (int i = 0; i < files.length; i++) {
                    createCompressedFile(out, files[i], dir + files[i].getName()); // 递归处理
                }
            } else { // 当前的是文件，打包处理
                // 文件输入流
                FileInputStream fis = new FileInputStream(file);

                out.putNextEntry(new ZipEntry(dir));
                // 进行写操作
                int j = 0;
                byte[] buffer = new byte[1024];
                while ((j = fis.read(buffer)) > 0) {
                    out.write(buffer, 0, j);
                }
                // 关闭输入流
                fis.close();
            }
        } catch (FileNotFoundException e) {
            throw new SystemException(e);
        } catch (IOException e) {
            throw new SystemException(e);
        } finally {

        }
    }

    List<StlSysParam> getSysParams(String tenantId, String typeCode, String paramCode) {
        StringBuilder key = new StringBuilder();
        key.append(tenantId);
        key.append(".");
        key.append(typeCode);
        key.append(".");
        key.append(paramCode);
        String data = sysParamCacheClient.get(key.toString());
        if (StringUtil.isBlank(data)) {
            return null;
        }
        return JSONArray.parseArray(data, StlSysParam.class);
    }

    StlSysParam getSysParam(String tenantId, String typeCode, String paramCode, String columnValue) {
        StringBuilder key = new StringBuilder();
        key.append(tenantId);
        key.append(".");
        key.append(typeCode);
        key.append(".");
        key.append(paramCode);
        key.append(".");
        key.append(columnValue);
        String data = sysParamCacheClient.get(key.toString());
        if (StringUtil.isBlank(data)) {
            return null;
        }
        return JSON.parseArray(data, StlSysParam.class).get(0);
    }

    String getSysParamDesc(String tenantId, String typeCode, String paramCode, String columnValue) {
        StlSysParam sysParam = getSysParam(tenantId, typeCode, paramCode, columnValue);
        return sysParam == null ? "" : sysParam.getColumnDesc();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(outputFields));
    }

}
