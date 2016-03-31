package com.ai.baas.smc.check.topology.core.bolt;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.baas.smc.check.topology.constants.SmcCacheConstant;
import com.ai.baas.smc.check.topology.vo.StlBillDetailStyleItem;
import com.ai.baas.smc.check.topology.vo.StlBillStyle;
import com.ai.baas.smc.check.topology.vo.StlElement;
import com.ai.baas.smc.check.topology.vo.StlPolicy;
import com.ai.baas.storm.message.MappingRule;
import com.ai.baas.storm.message.MessageParser;
import com.ai.baas.storm.util.BaseConstants;
import com.ai.baas.storm.util.HBaseProxy;
import com.ai.opt.base.exception.BusinessException;
import com.ai.opt.base.exception.SystemException;
import com.ai.opt.sdk.cache.factory.CacheClientFactory;
import com.ai.opt.sdk.util.StringUtil;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;
import com.alibaba.fastjson.JSON;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

/**
 * 校验<br>
 * Date: 2016年3月25日 <br>
 * Copyright (c) 2016 asiainfo.com <br>
 * 
 * @author mayt
 */
public class DataValidationBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 8475030105476807164L;

    private static Logger LOG = LoggerFactory.getLogger(DataValidationBolt.class);

    private ICacheClient cacheClient;

    private MappingRule[] mappingRules = new MappingRule[2];

    private String[] outputFields = new String[] { "data" };

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        LOG.info("数据校验bolt[prepare方法]...");
        super.prepare(stormConf, context);
        if (cacheClient == null) {
            cacheClient = CacheClientFactory.getCacheClient(SmcCacheConstant.NameSpace.RULE_MANAGE);
        }
        /* 初始化hbase */
        HBaseProxy.loadResource(stormConf);
        /* 2.获取报文格式信息 */
        mappingRules[0] = MappingRule.getMappingRule(MappingRule.FORMAT_TYPE_OUTPUT,
                BaseConstants.JDBC_DEFAULT);
        mappingRules[1] = mappingRules[0];
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        /* 接收输入报文 */
        String inputData = input.getString(0);
        LOG.info("数据校验bolt输入消息报文：[" + inputData + "]...");
        /* 解析报文 */
        MessageParser messageParser = null;
        try {
            messageParser = MessageParser.parseObject(inputData, mappingRules, outputFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> data = messageParser.getData();
        String line = input.getStringByField(BaseConstants.RECORD_DATA);
        LOG.info("-------------------line==" + line);
        String policyCode = "";
        String tenantId = data.get(BaseConstants.TENANT_ID);
        // 根据政策编码获取账单格式定义
        String policyStr = cacheClient.hget(SmcCacheConstant.NameSpace.RULE_MANAGE, tenantId + "."
                + policyCode);
        if (StringUtil.isBlank(policyStr)) {
            throw new SystemException("政策编码["+policyCode+"]定义不存在");
        }
        StlPolicy stlPolicy = JSON.parseObject(policyStr, StlPolicy.class);
        String billStyleSn = stlPolicy.getBillStyleSn();
        /* 获取此账单格式定义下的所有的详单项定义； */
        String billDetailStyleStr = cacheClient.hget(SmcCacheConstant.NameSpace.RULE_MANAGE,
                tenantId + "." + billStyleSn + "." + SmcCacheConstant.BILL_DETAIL_ITEM);
        if (StringUtil.isBlank(billDetailStyleStr)) {
            throw new SystemException("账单样式编码["+billStyleSn+"]详单项定义不存在");
        }
        List<StlBillDetailStyleItem> stlBillDetailStyleItems = JSON.parseArray(billDetailStyleStr,
                StlBillDetailStyleItem.class);
        /* 判断本详单数据是否满足详单项的格式定义（是否必填，取值类型）。 */
        for(int i =0;i<stlBillDetailStyleItems.size();i++){
            StlBillDetailStyleItem stlBillDetailStyleItem =stlBillDetailStyleItems.get(i);
            Long elementId = stlBillDetailStyleItem.getElementId();
            StringBuilder key = new StringBuilder();
            key.append(stlBillDetailStyleItem.getTenantId()).append(".").append(elementId);
            String elementStr =cacheClient.hget(SmcCacheConstant.NameSpace.RULE_MANAGE, key.toString());
            if(StringUtil.isBlank(elementStr)){
                throw new SystemException("元素ID["+elementId+"]不存在");
            }
            StlElement stlElement =JSON.parseObject(elementStr, StlElement.class);
            
        }
        /* 如果不满足则记录原因，沉淀数据库（在对应的详单数据记录后面增加一下校验结果【失败】及失败原因），并发送数据校验异常消息。 */
        /* 如果满足，则沉淀数据库（在对应的详单数据记录后面增加一下校验结果:通过），并发送数据校验通过消息。 */
        /* 更新结算详单校验计数器（redis）的校验通过记录数和校验失败记录数。 */

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub

    }
}
