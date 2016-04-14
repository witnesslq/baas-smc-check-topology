package com.ai.baas.smc.check.topology.core.bolt;

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
import com.ai.baas.smc.check.topology.constants.SmcCacheConstant;
import com.ai.baas.smc.check.topology.constants.SmcCacheConstant.Dshm.FieldName;
import com.ai.baas.smc.check.topology.constants.SmcConstant;
import com.ai.baas.smc.check.topology.constants.SmcExceptCodeConstant;
import com.ai.baas.smc.check.topology.constants.SmcHbaseConstant;
import com.ai.baas.smc.check.topology.vo.StlBillData;
import com.ai.baas.smc.check.topology.vo.StlBillStyleItem;
import com.ai.baas.smc.check.topology.vo.StlPolicy;
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

/**
 * 对账<br>
 * Date: 2016年3月25日 <br>
 * Copyright (c) 2016 asiainfo.com <br>
 * 
 * @author mayt
 */
public class BillDetailCheckBolt extends BaseBasicBolt {

    private static final long serialVersionUID = -3214008757998306486L;

    ICacheClient policyCacheClient;

    private ICacheClient billStyleCacheClient;

    private String[] outputFields = new String[] { "data" };

    private MappingRule[] mappingRules = new MappingRule[2];

    private StlBillDataDAO stlBillDataDAO;

    IDshmClient dshmClient;

    ICacheClient calParamCacheClient;

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
        mappingRules[0] = MappingRule.getMappingRule(MappingRule.FORMAT_TYPE_OUTPUT,
                BaseConstants.JDBC_DEFAULT);
        mappingRules[1] = mappingRules[0];
        /* 3.初始化hbase */
        HBaseProxy.loadResource(stormConf);
        if (stlBillDataDAO == null) {
            stlBillDataDAO = new StlBillDataDAO();
        }
        if (dshmClient == null) {
            dshmClient = new DshmClient();
        }
        if (calParamCacheClient == null) {
            calParamCacheClient = CacheFactoryUtil.getCacheClient(CacheBLMapper.CACHE_BL_CAL_PARAM);
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
            Table table = HBaseProxy.getConnection().getTable(
                    TableName.valueOf(SmcHbaseConstant.TableName.STL_BILL_DETAIL_DATA_ + yyyyMm));
            ResultScanner resultScanner = table.getScanner(scan);
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
                if (!StringUtil.isBlank(feeItemIdSys) && !StringUtil.isBlank(itemFeeSys)
                        && feeItemId3pl.equals(feeItemIdSys)) {
                    diffFee = String.valueOf(Long.parseLong(itemFee3pl)
                            - Long.parseLong(itemFeeSys));
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
                resultScanner = table.getScanner(scan);
                result = resultScanner.next();
                if (result == null) {
                    throw new BusinessException(SmcExceptCodeConstant.BUSINESS_EXCEPTION,
                            "第三方详单数据不存在[rowKey:" + rowKey + "]");
                }
                // 写入差异表
                NavigableMap<byte[], byte[]> map = result
                        .getFamilyMap(SmcHbaseConstant.FamilyName.COLUMN_DEF.getBytes());
                String billDetailId = new String(map.get(SmcHbaseConstant.ColumnName.BILL_DETAIL_ID
                        .getBytes()));
                rowKey = rowKey + billDetailId;
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
                table.put(put);
            }
            // 6， 本账单对账次数加1（redis），如果对账次数＝第三方账单详单记录数，则说明第三方详单都已对账完成：
            // a) 查询本系统结算算费结果详单，查询本系统存在记录，而第三方详单不存在的记录，把
            // 些记录插入差异详单表（stl_bill_detail_diff_data_yyyymm）
            // b) 修改账单数据表（第三方账单和本系统结算算费结果帐单）中的对账结果（差异金额为0则沉淀状态为账单一致，否则沉淀状态为有差异）。
            // 7， 如果对账结果为有差异，则调用对账错误详单文件生成方法，生成错误详单文件，并向账详单处理结果文件清单表新增记录。
            // 8， 完成
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

}
