package com.ai.baas.smc.check.topology.test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ai.baas.dshm.client.CacheFactoryUtil;
import com.ai.baas.dshm.client.impl.CacheBLMapper;
import com.ai.baas.dshm.client.impl.DshmClient;
import com.ai.baas.smc.check.topology.constants.SmcCacheConstant;
import com.ai.baas.smc.check.topology.constants.SmcExceptCodeConstant;
import com.ai.baas.smc.check.topology.constants.SmcCacheConstant.Dshm.FieldName;
import com.ai.opt.base.exception.BusinessException;
import com.ai.opt.sdk.util.CollectionUtil;
import com.ai.opt.sdk.util.StringUtil;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;

public class TestTopology {

    public static void main(String[] args) {

        // 查询导入日志
        Map<String, String> params = new TreeMap<String, String>();
        params.put(SmcCacheConstant.Dshm.FieldName.TENANT_ID, "tenantId");
        params.put(SmcCacheConstant.Dshm.FieldName.BATCH_NO, "1234");
        DshmClient dshmClient = new DshmClient();
        ICacheClient calParamCacheClient = CacheFactoryUtil
                .getCacheClient(CacheBLMapper.CACHE_BL_CAL_PARAM);
        List<Map<String, String>> results = dshmClient
                .list(SmcCacheConstant.Dshm.TableName.STL_IMPORT_LOG).where(params)
                .executeQuery(calParamCacheClient);
        if (CollectionUtil.isEmpty(results)) {
            throw new BusinessException(SmcExceptCodeConstant.BUSINESS_EXCEPTION, "导入日志不存在");
        }
        Map<String, String> importLogMap = results.get(0);// 导入日志map
        String billTimeSn = importLogMap.get(FieldName.BILL_TIME_SN);// 账期
        System.out.println(" ====== billTimeSn = " + billTimeSn);
        String yyyyMm = StringUtil.restrictLength(billTimeSn, 6);
        System.out.println(yyyyMm);
    }

}
