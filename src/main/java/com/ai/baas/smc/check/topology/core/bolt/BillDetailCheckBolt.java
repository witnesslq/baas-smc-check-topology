package com.ai.baas.smc.check.topology.core.bolt;

import java.util.List;
import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.ai.baas.smc.check.topology.constants.SmcCacheConstant;
import com.ai.baas.smc.check.topology.constants.SmcExceptCodeConstant;
import com.ai.baas.smc.check.topology.vo.StlBillStyleItem;
import com.ai.baas.smc.check.topology.vo.StlPolicy;
import com.ai.opt.base.exception.BusinessException;
import com.ai.opt.base.exception.SystemException;
import com.ai.opt.sdk.cache.factory.CacheClientFactory;
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
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String policyCode = "";
        String tenantId = "";
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
        List<StlBillStyleItem> stlBillStyleItems = JSON
                .parseArray(cacheStr, StlBillStyleItem.class);
//        3，  根据详单项配置解析本详单数据；
        
//        4，  根据本详单的租户、流水号、政策编码、账期获取本系统结算算费结果详单数据
//        5，  如果不存在此流水或此流水对应的科目金额不一致，向详单差异表中插入此差异详单表（stl_bill_detail_diff_data_yyyymm）。
//        6，  本账单对账次数加1（redis），如果对账次数＝第三方账单详单记录数，则说明第三方详单都已对账完成：
//        a)  查询本系统结算算费结果详单，查询本系统存在记录，而第三方详单不存在的记录，把 些记录插入差异详单表（stl_bill_detail_diff_data_yyyymm）
//        b)  修改账单数据表（第三方账单和本系统结算算费结果帐单）中的对账结果（差异金额为0则沉淀状态为账单一致，否则沉淀状态为有差异）。
//        7，  如果对账结果为有差异，则调用对账错误详单文件生成方法，生成错误详单文件，并向账详单处理结果文件清单表新增记录。
//        8，  完成

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

}
