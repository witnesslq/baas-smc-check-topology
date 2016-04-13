package com.ai.baas.smc.check.topology.DAO;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.ai.baas.smc.check.topology.vo.StlBillData;
import com.ai.baas.storm.jdbc.JdbcTemplate;

public class StlBillDataDAO {
    public List<StlBillData> query(Connection conn, String yyyyMm, String tenantId, String batchNo) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" select BILL_ID billId, BILL_FROM billFrom, BATCH_NO batchNo, ");
        sqlBuilder
                .append(" TENANT_ID tenantId, POLICY_CODE policyCode, STL_OBJECT_ID stlObjectId, ");
        sqlBuilder.append(" STL_ELEMENT_ID stlElementId, STL_ELEMENT_SN stlElementSn, ");
        sqlBuilder.append(" BILL_STYLE_SN billStyleSn, BILL_TIME_SN billTimeSn, ");
        sqlBuilder.append(" BILL_START_TIME billStartTime, BILL_END_TIME billEndTime, ");
        sqlBuilder.append(" ORIG_FEE origFee, CHECK_STATE checkState, DIFF_FEE diffFee, ");
        sqlBuilder.append(" CHECK_STATE_DESC checkStateDesc, CHECK_TIME checkTime, ");
        sqlBuilder.append(" ADJUST_FEE adjustFee, FINAL_FEE finalFee, ADJUST_TIME adjustTime, ");
        sqlBuilder
                .append(" ADJUST_OPER_ID adjustOperId, ADJUST_DESC adjustDesc, EXTEND_STR extendStr,");
        sqlBuilder
                .append(" CREATE_DEPT_ID createDeptId, CREATE_OPER_ID createOperId, CREATE_TIME createTime");
        sqlBuilder.append(" from stl_bill_data_").append(yyyyMm);
        sqlBuilder.append(" where tenant_id = ").append(tenantId);
        sqlBuilder.append(" and batch_no = ").append(batchNo);

        return JdbcTemplate.query(sqlBuilder.toString(), conn, new BeanListHandler<>(
                StlBillData.class));
    }
}
