package com.ai.baas.smc.check.topology.core.bolt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import com.ai.baas.smc.check.topology.constants.SmcConstant;
import com.ai.baas.smc.check.topology.constants.SmcExceptCodeConstant;
import com.ai.baas.smc.check.topology.constants.SmcHbaseConstant;
import com.ai.baas.storm.duplicate.DuplicateCheckingConfig;
import com.ai.baas.storm.duplicate.DuplicateCheckingFromHBase;
import com.ai.baas.storm.failbill.FailBillHandler;
import com.ai.baas.storm.jdbc.JdbcProxy;
import com.ai.baas.storm.message.MappingRule;
import com.ai.baas.storm.message.MessageParser;
import com.ai.baas.storm.util.BaseConstants;
import com.ai.opt.base.exception.BusinessException;

public class DuplicateCheckingBolt extends BaseBasicBolt {
    private static final long serialVersionUID = -4549737615575118377L;

    private static Logger LOG = LoggerFactory.getLogger(DuplicateCheckingBolt.class);

    private String[] outputFields = new String[] { "data" };

    private MappingRule[] mappingRules = new MappingRule[2];

    public DuplicateCheckingBolt() {
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        System.out.println(" ====== DuplicateCheckingBolt.prepare");
        JdbcProxy.loadResources(Arrays.asList(BaseConstants.JDBC_DEFAULT), stormConf);
        DuplicateCheckingConfig.getInstance();
        mappingRules[0] = MappingRule.getMappingRule(MappingRule.FORMAT_TYPE_OUTPUT,
                BaseConstants.JDBC_DEFAULT);
        mappingRules[1] = mappingRules[0];
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        LOG.info("查重bolt[execute方法]...");
        /* 1.接收输入报文 */
        String inputData = null;
        Map<String, String> data = null;
        try {
            /* 1.接收输入报文 */
            inputData = input.getString(0);
            LOG.info("查重bolt输入消息报文：[" + inputData + "]...");
            /* 2.解析报文 */
            MessageParser messageParser = MessageParser.parseObject(inputData, mappingRules,
                    outputFields);
            data = messageParser.getData();
            data.put(SmcHbaseConstant.ColumnName.TOTAL_FEE, String.valueOf(Long.parseLong(data
                    .get(SmcHbaseConstant.ColumnName.TOTAL_FEE)) * 1000));
            for (Entry<String, String> entrys : data.entrySet()) {
                LOG.info("data key:" + entrys.getKey() + " , value:" + entrys.getValue());
            }
            /* 3.查重 */
            DuplicateCheckingFromHBase checking = new DuplicateCheckingFromHBase();
            if (!checking.checkData(data)) {
                throw new BusinessException(SmcExceptCodeConstant.FAIL_CODE_DUP, "重复流水");
            }
            /* 5.将报文输出到下一环节 */
            List<Object> datas = new ArrayList<Object>();
            datas.add(inputData);
            collector.emit(datas);
        } catch (BusinessException e) {
            /* 6.进重单表 */
            LOG.error("查重bolt出现异常", e);
            FailBillHandler.addFailBillMsg(data, SmcConstant.DUPLICATE_CHECK_BOLT,
                    e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            LOG.error("查重bolt出现异常", e);
            FailBillHandler.addFailBillMsg(data, SmcConstant.DATA_VALIDATION_BOLT,
                    SmcExceptCodeConstant.SYSTEM_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(outputFields));
    }

}
