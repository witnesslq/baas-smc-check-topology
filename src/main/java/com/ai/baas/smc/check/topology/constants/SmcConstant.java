package com.ai.baas.smc.check.topology.constants;

public class SmcConstant {

    public static final String UNPACKING_BOLT = "unpacking";

    public static final String DATA_VALIDATION_BOLT = "data_validation";

    public static final String BILL_DETAIL_CHECK_BOLT = "bill_detail_checking";

    public static class FmtFeildName {
        public static final String BATCH_NO = "batch_no";

        public static final String TOTAL_RECORD = "total_record";

        public static final String TENANT_ID = "tenant_id";

        public static final String ORDER_ID = "order_id";

        public static final String FEE_ITEM_ID = "fee_item_id";

        public static final String TOTAL_FEE = "total_fee";

    }

    public static class StlBillData {
        public static class BillFrom {
            /**
             * 系统生成
             */
            public static final String SYS = "sys";

            /**
             * 第三方导入
             */
            public static final String IMPORT = "3pl";
        }

        public static class CheckState {
            /**
             * 一致
             */
            public static final String UNANIMOUS = "3";

            /**
             * 不一致
             */
            public static final String INCONFORMITY = "4";
        }

        public static class CheckStateDesc {
            /**
             * 账单一致
             */
            public static final String BILL_UNANIMOUS = "账单一致";

            /**
             * 有差异
             */
            public static final String HAS_DIFFERENCE = "有差异";
        }
    }
    public static class StlBillDetailDiffData {
        public static class CheckState {
            /**
             * 一致
             */
            public static final String SAME = "1";

            /**
             * 不一致
             */
            public static final String DIFF = "2";
        }
    }
}
