package com.ai.baas.smc.check.topology.constants;

public class SmcConstant {
    public final static String CHARSET_UTF8 = "utf-8";

    public final static String CHARSET_GBK = "gbk";

    public static final String FIELD_SPLIT = new String(new char[] { (char) 1 });

    public static final String RECORD_SPLIT = new String(new char[] { (char) 2 });

    public static final String HEADER_SPLIT = new String(new char[] { (char) 3 });

    public static final String CVSFILE_FEILD_SPLIT = ",";

    public static final String UNPACKING_BOLT = "unpackageing";

    public static final String DATA_VALIDATION_BOLT = "data_validation";

    public static final String BILL_DETAIL_CHECK_BOLT = "bill_detail_checking";

    public static final String DUPLICATE_CHECK_BOLT = "duplicate_checking";

    /**
     * 基础元素表<br>
     * Date: 2016年3月17日 <br>
     * Copyright (c) 2016 asiainfo.com <br>
     * 
     * @author mayt
     */
    public static class StlElement {
        /**
         * 是否结算对象<br>
         * 1：是 0：否<br>
         * Date: 2016年3月17日 <br>
         * Copyright (c) 2016 asiainfo.com <br>
         * 
         * @author mayt
         */
        public static class IsSettlement {
            public static final String YES = "1";

            public static final String NO = "0";
        }

        /**
         * 状态<br>
         * Date: 2016年3月17日 <br>
         * Copyright (c) 2016 asiainfo.com <br>
         * 
         * @author mayt
         */
        public static class State {
            /**
             * 正常
             */
            public static final String NORMAL = "1";

            /**
             * 注销
             */
            public static final String CANCELLED = "0";
        }

        public static class ValueType {

            /**
             * 整数
             */
            public static final String INT = "int";

            /**
             * float
             */
            public static final String FLOAT = "float";

            public static final String DATE_TIME = "datetime";

            /**
             * 枚举
             */
            public static final String ENUM = "enum";

            /**
             * string
             */
            public static final String STRING = "string";
        }

        /**
         * 是否必填<br>
         * Date: 2016年4月16日 <br>
         * Copyright (c) 2016 asiainfo.com <br>
         * 
         * @author mayt
         */
        public static class IsNecessary {
            public static final String YES = "1";

            public static final String NO = "0";
        }
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

    public static class StlBillItemData {
        public static class ItemType {
            /**
             * 正常科目
             */
            public static final String NORMAL = "1";

            /**
             * 调账科目
             */
            public static final String ADJUST = "2";
        }

        public static class CheckState {
            /**
             * 一致
             */
            public static final String UNANIMOUS = "1";

            /**
             * 不一致
             */
            public static final String INCONFORMITY = "2";
        }

        public static class CheckStateDesc {
            /**
             * 一致
             */
            public static final String UNANIMOUS = "一致";

            /**
             * 不一致
             */
            public static final String INCONFORMITY = "不一致";
        }

        public static class VerifyState {
            /**
             * 通过
             */
            public static final String PASS = "1";

            /**
             * 不通过
             */
            public static final String NOT_PASS = "0";
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
