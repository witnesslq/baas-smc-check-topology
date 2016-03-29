package com.ai.baas.smc.check.topology.test;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.ai.baas.dshm.client.CacheFactoryUtil;
import com.ai.baas.dshm.client.impl.CacheBLMapper;
import com.ai.baas.dshm.client.impl.DshmClient;
import com.ai.baas.dshm.client.interfaces.IDshmClient;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;

public class DshmDemo {
    public static  void test() {
        IDshmClient client=null;
        if(client==null)
          client=new DshmClient();
        ICacheClient cacheClient =  CacheFactoryUtil
                   .getCacheClient(CacheBLMapper.CACHE_BL_CAL_PARAM);
        Map<String,String> params = new TreeMap<String,String>();
        params.put("price_code", "999"); 
        params.put("tenant_id", "VIV-BYD");
        List<Map<String, String>> results=client.list("cp_price_info")
             .where(params)
             .executeQuery(cacheClient);
        for (Map<String, String> map : results){
          for(Entry<String, String> result:map.entrySet()){
             System.out.println("the key is "+result.getKey()+"="+result.getValue());
          }
        }
    }
    public static void main(String[] args){
        test();
    }
}
