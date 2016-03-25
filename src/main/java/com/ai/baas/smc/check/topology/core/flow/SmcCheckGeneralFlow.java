package com.ai.baas.smc.check.topology.core.flow;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.baas.smc.check.topology.constants.SmcCheckConstant;
import com.ai.baas.smc.check.topology.core.bolt.DuplicateCheckingBolt;
import com.ai.baas.smc.check.topology.core.bolt.UnpackingBolt;
import com.ai.baas.storm.flow.BaseFlow;
import com.ai.baas.storm.util.BaseConstants;

public class SmcCheckGeneralFlow extends BaseFlow {
    private static final Logger LOG = LoggerFactory.getLogger(SmcCheckGeneralFlow.class);

    @Override
    @SuppressWarnings("unchecked")
    public void define() {
        super.setKafkaSpout();
        Map<String, String> outputFieldMapping = (Map<String, String>) conf
                .get("bmc.gprs.bolt.output.field");
        builder.setBolt(SmcCheckConstant.UNPACKING_BOLT,
                new UnpackingBolt(outputFieldMapping.get(SmcCheckConstant.UNPACKING_BOLT)), 1)
                .shuffleGrouping(BaseConstants.KAFKA_SPOUT_NAME);
        builder.setBolt(
                "duplicate-checking",
                new DuplicateCheckingBolt(outputFieldMapping
                        .get(SmcCheckConstant.DUPLICATE_CHECKING_BOLT)), 1).shuffleGrouping(
                SmcCheckConstant.UNPACKING_BOLT);

    }

    public static void main(String[] args) {
        SmcCheckGeneralFlow flow = new SmcCheckGeneralFlow();
        flow.run(args);
    }

}
