package com.ai.baas.smc.check.topology.core.bolt;

import java.util.Map;

import com.ai.baas.smc.check.topology.constants.SmcCacheConstant;
import com.ai.opt.base.exception.BusinessException;
import com.ai.opt.sdk.cache.factory.CacheClientFactory;
import com.ai.opt.sdk.util.StringUtil;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

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
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
        if(policyCacheClient==null){
         policyCacheClient=CacheClientFactory.getCacheClient(SmcCacheConstant.NameSpace.POLICY_CACHE);}
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
String policyCode="";
String tenantId ="";
StringBuilder key =new StringBuilder();
key.append(tenantId).append(".").append(policyCode);
String policyStr =policyCacheClient.get(key.toString());
if(StringUtil.isBlank(policyStr)){
    throw new BusinessException(, errorMessage)
}
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

}
