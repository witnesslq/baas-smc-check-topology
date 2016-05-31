package com.ai.baas.smc.check.topology.test;

import com.ai.opt.sdk.components.mcs.MCSClientFactory;
import com.ai.paas.ipaas.mcs.CacheFactory;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;

public class Test {

    public static void main(String[] args) throws Exception {
        AuthDescriptor authDescriptor = new AuthDescriptor(
                "http://10.1.245.4:19811/service-portal-uac-web/service/auth",
                "87EA5A771D9647F1B5EBB600812E3067", "123456", "MCS001");
        ICacheClient client = CacheFactory.getClient(authDescriptor);
        ICacheClient calParamCacheClient = MCSClientFactory
                .getCacheClient("com.ai.runner.center.dshm.cache.calparam");
        if (calParamCacheClient == null) {
            System.out.println("null");
        }
        System.out.println(0);
    }

}
