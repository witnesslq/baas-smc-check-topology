package com.ai.baas.smc.check.topology.core.bolt;

import java.util.Map;

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

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        // TODO Auto-generated method stub
        super.prepare(stormConf, context);
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        // TODO Auto-generated method stub

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub

    }
}
