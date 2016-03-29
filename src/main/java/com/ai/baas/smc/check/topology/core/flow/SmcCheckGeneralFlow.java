package com.ai.baas.smc.check.topology.core.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.baas.smc.check.topology.constants.SmcCheckConstant;
import com.ai.baas.smc.check.topology.core.bolt.BillDetailCheckBolt;
import com.ai.baas.smc.check.topology.core.bolt.DataValidationBolt;
import com.ai.baas.smc.check.topology.core.bolt.UnpackingBolt;
import com.ai.baas.storm.flow.BaseFlow;
import com.ai.baas.storm.util.BaseConstants;

public class SmcCheckGeneralFlow extends BaseFlow {
    private static final Logger LOG = LoggerFactory.getLogger(SmcCheckGeneralFlow.class);

    @Override
    public void define() {
        super.setKafkaSpout();
        /* 解包bolt */
        builder.setBolt(SmcCheckConstant.UNPACKING_BOLT, new UnpackingBolt(), 1).shuffleGrouping(
                BaseConstants.KAFKA_SPOUT_NAME);
        /* 数据校验bolt */
        builder.setBolt(SmcCheckConstant.DATA_VALIDATION_BOLT, new DataValidationBolt(), 1)
                .shuffleGrouping(BaseConstants.KAFKA_SPOUT_NAME);
        /* 对账bolt */
        builder.setBolt(SmcCheckConstant.BILL_DETAIL_CHECK_BOLT, new BillDetailCheckBolt(), 1)
                .shuffleGrouping(BaseConstants.KAFKA_SPOUT_NAME);
    }

    public static void main(String[] args) {
        LOG.info("开始启动结算对账拓扑");
        SmcCheckGeneralFlow flow = new SmcCheckGeneralFlow();
        flow.run(args);
    }

}
