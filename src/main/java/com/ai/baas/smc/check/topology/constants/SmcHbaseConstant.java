package com.ai.baas.smc.check.topology.constants;

public final class SmcHbaseConstant {
    private SmcHbaseConstant() {
    }

    public static final String ROWKEY_SPLIT = "_";

    public static class TableName {
        public static final String STL_BILL_DETAIL_DIFF_DATA_ = "stl_bill_detail_diff_data_";

        /**
         * 详单数据表
         */
        public static final String STL_BILL_DETAIL_DATA_ = "stl_bill_detail_data_";

    }

    public static class ColumnName {

        public static final String FEE_ITEM_ID = "fee_item_id";

        public static final String ITEM_FEE = "item_fee";

        public static final String BILL_DETAIL_ID = "bill_detail_id";

        public static final String DIFF_FEE = "diff_fee";

        public static final String CHECK_STATE = "check_state";

        public static final String ORDER_ID = "order_id";

        public static final String STL_ORDER_DATA_KEY = "stl_order_data_key";

        public static final String CHECK_STATE_DESC = "check_state_desc";

        public static final String VERIFY_STATE = "verify_state";

        public static final String VERIFY_DESC = "verify_desc";

        public static final String BILL_ID = "bill_id";

        public static final String TENANT_ID = "tenant_id";

    }

    /**
     * 列族<br>
     * Date: 2016年4月14日 <br>
     * Copyright (c) 2016 asiainfo.com <br>
     * 
     * @author mayt
     */
    public static class FamilyName {
        /**
         * 默认列族名
         */
        public static final String COLUMN_DEF = "col_def";
    }
}
