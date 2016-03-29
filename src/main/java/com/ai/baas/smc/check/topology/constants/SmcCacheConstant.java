package com.ai.baas.smc.check.topology.constants;

public final class SmcCacheConstant {
    private SmcCacheConstant() {
    }

    /**
     * 账单项
     */
    public static final String BILL_ITEM = "bill.item";

    /**
     * 详单项
     */
    public static final String BILL_DETAIL_ITEM = "bill.detail.item";

    public static final class TypeCode {
        private TypeCode() {
        }

    }

    public static final class ParamCode {
        private ParamCode() {
        }

    }

    public static final class NameSpace {

        private NameSpace() {
        }

        /**
         * sys_param
         */
        public static final String SYS_PARAM_CACHE = "com.ai.baas.smc.cache.sysparam";

        public static final String RULE_MANAGE = "com.ai.baas.smc.cache.rulemanage";
    }
}
