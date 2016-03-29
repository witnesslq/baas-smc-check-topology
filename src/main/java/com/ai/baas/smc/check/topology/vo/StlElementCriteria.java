package com.ai.baas.smc.check.topology.vo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class StlElementCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public StlElementCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimitStart(Integer limitStart) {
        this.limitStart=limitStart;
    }

    public Integer getLimitStart() {
        return limitStart;
    }

    public void setLimitEnd(Integer limitEnd) {
        this.limitEnd=limitEnd;
    }

    public Integer getLimitEnd() {
        return limitEnd;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andElementIdIsNull() {
            addCriterion("ELEMENT_ID is null");
            return (Criteria) this;
        }

        public Criteria andElementIdIsNotNull() {
            addCriterion("ELEMENT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andElementIdEqualTo(Long value) {
            addCriterion("ELEMENT_ID =", value, "elementId");
            return (Criteria) this;
        }

        public Criteria andElementIdNotEqualTo(Long value) {
            addCriterion("ELEMENT_ID <>", value, "elementId");
            return (Criteria) this;
        }

        public Criteria andElementIdGreaterThan(Long value) {
            addCriterion("ELEMENT_ID >", value, "elementId");
            return (Criteria) this;
        }

        public Criteria andElementIdGreaterThanOrEqualTo(Long value) {
            addCriterion("ELEMENT_ID >=", value, "elementId");
            return (Criteria) this;
        }

        public Criteria andElementIdLessThan(Long value) {
            addCriterion("ELEMENT_ID <", value, "elementId");
            return (Criteria) this;
        }

        public Criteria andElementIdLessThanOrEqualTo(Long value) {
            addCriterion("ELEMENT_ID <=", value, "elementId");
            return (Criteria) this;
        }

        public Criteria andElementIdIn(List<Long> values) {
            addCriterion("ELEMENT_ID in", values, "elementId");
            return (Criteria) this;
        }

        public Criteria andElementIdNotIn(List<Long> values) {
            addCriterion("ELEMENT_ID not in", values, "elementId");
            return (Criteria) this;
        }

        public Criteria andElementIdBetween(Long value1, Long value2) {
            addCriterion("ELEMENT_ID between", value1, value2, "elementId");
            return (Criteria) this;
        }

        public Criteria andElementIdNotBetween(Long value1, Long value2) {
            addCriterion("ELEMENT_ID not between", value1, value2, "elementId");
            return (Criteria) this;
        }

        public Criteria andTenantIdIsNull() {
            addCriterion("TENANT_ID is null");
            return (Criteria) this;
        }

        public Criteria andTenantIdIsNotNull() {
            addCriterion("TENANT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andTenantIdEqualTo(String value) {
            addCriterion("TENANT_ID =", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdNotEqualTo(String value) {
            addCriterion("TENANT_ID <>", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdGreaterThan(String value) {
            addCriterion("TENANT_ID >", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdGreaterThanOrEqualTo(String value) {
            addCriterion("TENANT_ID >=", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdLessThan(String value) {
            addCriterion("TENANT_ID <", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdLessThanOrEqualTo(String value) {
            addCriterion("TENANT_ID <=", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdLike(String value) {
            addCriterion("TENANT_ID like", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdNotLike(String value) {
            addCriterion("TENANT_ID not like", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdIn(List<String> values) {
            addCriterion("TENANT_ID in", values, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdNotIn(List<String> values) {
            addCriterion("TENANT_ID not in", values, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdBetween(String value1, String value2) {
            addCriterion("TENANT_ID between", value1, value2, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdNotBetween(String value1, String value2) {
            addCriterion("TENANT_ID not between", value1, value2, "tenantId");
            return (Criteria) this;
        }

        public Criteria andElementCodeIsNull() {
            addCriterion("ELEMENT_CODE is null");
            return (Criteria) this;
        }

        public Criteria andElementCodeIsNotNull() {
            addCriterion("ELEMENT_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andElementCodeEqualTo(String value) {
            addCriterion("ELEMENT_CODE =", value, "elementCode");
            return (Criteria) this;
        }

        public Criteria andElementCodeNotEqualTo(String value) {
            addCriterion("ELEMENT_CODE <>", value, "elementCode");
            return (Criteria) this;
        }

        public Criteria andElementCodeGreaterThan(String value) {
            addCriterion("ELEMENT_CODE >", value, "elementCode");
            return (Criteria) this;
        }

        public Criteria andElementCodeGreaterThanOrEqualTo(String value) {
            addCriterion("ELEMENT_CODE >=", value, "elementCode");
            return (Criteria) this;
        }

        public Criteria andElementCodeLessThan(String value) {
            addCriterion("ELEMENT_CODE <", value, "elementCode");
            return (Criteria) this;
        }

        public Criteria andElementCodeLessThanOrEqualTo(String value) {
            addCriterion("ELEMENT_CODE <=", value, "elementCode");
            return (Criteria) this;
        }

        public Criteria andElementCodeLike(String value) {
            addCriterion("ELEMENT_CODE like", value, "elementCode");
            return (Criteria) this;
        }

        public Criteria andElementCodeNotLike(String value) {
            addCriterion("ELEMENT_CODE not like", value, "elementCode");
            return (Criteria) this;
        }

        public Criteria andElementCodeIn(List<String> values) {
            addCriterion("ELEMENT_CODE in", values, "elementCode");
            return (Criteria) this;
        }

        public Criteria andElementCodeNotIn(List<String> values) {
            addCriterion("ELEMENT_CODE not in", values, "elementCode");
            return (Criteria) this;
        }

        public Criteria andElementCodeBetween(String value1, String value2) {
            addCriterion("ELEMENT_CODE between", value1, value2, "elementCode");
            return (Criteria) this;
        }

        public Criteria andElementCodeNotBetween(String value1, String value2) {
            addCriterion("ELEMENT_CODE not between", value1, value2, "elementCode");
            return (Criteria) this;
        }

        public Criteria andElementNameIsNull() {
            addCriterion("ELEMENT_NAME is null");
            return (Criteria) this;
        }

        public Criteria andElementNameIsNotNull() {
            addCriterion("ELEMENT_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andElementNameEqualTo(String value) {
            addCriterion("ELEMENT_NAME =", value, "elementName");
            return (Criteria) this;
        }

        public Criteria andElementNameNotEqualTo(String value) {
            addCriterion("ELEMENT_NAME <>", value, "elementName");
            return (Criteria) this;
        }

        public Criteria andElementNameGreaterThan(String value) {
            addCriterion("ELEMENT_NAME >", value, "elementName");
            return (Criteria) this;
        }

        public Criteria andElementNameGreaterThanOrEqualTo(String value) {
            addCriterion("ELEMENT_NAME >=", value, "elementName");
            return (Criteria) this;
        }

        public Criteria andElementNameLessThan(String value) {
            addCriterion("ELEMENT_NAME <", value, "elementName");
            return (Criteria) this;
        }

        public Criteria andElementNameLessThanOrEqualTo(String value) {
            addCriterion("ELEMENT_NAME <=", value, "elementName");
            return (Criteria) this;
        }

        public Criteria andElementNameLike(String value) {
            addCriterion("ELEMENT_NAME like", value, "elementName");
            return (Criteria) this;
        }

        public Criteria andElementNameNotLike(String value) {
            addCriterion("ELEMENT_NAME not like", value, "elementName");
            return (Criteria) this;
        }

        public Criteria andElementNameIn(List<String> values) {
            addCriterion("ELEMENT_NAME in", values, "elementName");
            return (Criteria) this;
        }

        public Criteria andElementNameNotIn(List<String> values) {
            addCriterion("ELEMENT_NAME not in", values, "elementName");
            return (Criteria) this;
        }

        public Criteria andElementNameBetween(String value1, String value2) {
            addCriterion("ELEMENT_NAME between", value1, value2, "elementName");
            return (Criteria) this;
        }

        public Criteria andElementNameNotBetween(String value1, String value2) {
            addCriterion("ELEMENT_NAME not between", value1, value2, "elementName");
            return (Criteria) this;
        }

        public Criteria andObjectIdIsNull() {
            addCriterion("OBJECT_ID is null");
            return (Criteria) this;
        }

        public Criteria andObjectIdIsNotNull() {
            addCriterion("OBJECT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andObjectIdEqualTo(String value) {
            addCriterion("OBJECT_ID =", value, "objectId");
            return (Criteria) this;
        }

        public Criteria andObjectIdNotEqualTo(String value) {
            addCriterion("OBJECT_ID <>", value, "objectId");
            return (Criteria) this;
        }

        public Criteria andObjectIdGreaterThan(String value) {
            addCriterion("OBJECT_ID >", value, "objectId");
            return (Criteria) this;
        }

        public Criteria andObjectIdGreaterThanOrEqualTo(String value) {
            addCriterion("OBJECT_ID >=", value, "objectId");
            return (Criteria) this;
        }

        public Criteria andObjectIdLessThan(String value) {
            addCriterion("OBJECT_ID <", value, "objectId");
            return (Criteria) this;
        }

        public Criteria andObjectIdLessThanOrEqualTo(String value) {
            addCriterion("OBJECT_ID <=", value, "objectId");
            return (Criteria) this;
        }

        public Criteria andObjectIdLike(String value) {
            addCriterion("OBJECT_ID like", value, "objectId");
            return (Criteria) this;
        }

        public Criteria andObjectIdNotLike(String value) {
            addCriterion("OBJECT_ID not like", value, "objectId");
            return (Criteria) this;
        }

        public Criteria andObjectIdIn(List<String> values) {
            addCriterion("OBJECT_ID in", values, "objectId");
            return (Criteria) this;
        }

        public Criteria andObjectIdNotIn(List<String> values) {
            addCriterion("OBJECT_ID not in", values, "objectId");
            return (Criteria) this;
        }

        public Criteria andObjectIdBetween(String value1, String value2) {
            addCriterion("OBJECT_ID between", value1, value2, "objectId");
            return (Criteria) this;
        }

        public Criteria andObjectIdNotBetween(String value1, String value2) {
            addCriterion("OBJECT_ID not between", value1, value2, "objectId");
            return (Criteria) this;
        }

        public Criteria andAttrTypeIsNull() {
            addCriterion("ATTR_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andAttrTypeIsNotNull() {
            addCriterion("ATTR_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andAttrTypeEqualTo(String value) {
            addCriterion("ATTR_TYPE =", value, "attrType");
            return (Criteria) this;
        }

        public Criteria andAttrTypeNotEqualTo(String value) {
            addCriterion("ATTR_TYPE <>", value, "attrType");
            return (Criteria) this;
        }

        public Criteria andAttrTypeGreaterThan(String value) {
            addCriterion("ATTR_TYPE >", value, "attrType");
            return (Criteria) this;
        }

        public Criteria andAttrTypeGreaterThanOrEqualTo(String value) {
            addCriterion("ATTR_TYPE >=", value, "attrType");
            return (Criteria) this;
        }

        public Criteria andAttrTypeLessThan(String value) {
            addCriterion("ATTR_TYPE <", value, "attrType");
            return (Criteria) this;
        }

        public Criteria andAttrTypeLessThanOrEqualTo(String value) {
            addCriterion("ATTR_TYPE <=", value, "attrType");
            return (Criteria) this;
        }

        public Criteria andAttrTypeLike(String value) {
            addCriterion("ATTR_TYPE like", value, "attrType");
            return (Criteria) this;
        }

        public Criteria andAttrTypeNotLike(String value) {
            addCriterion("ATTR_TYPE not like", value, "attrType");
            return (Criteria) this;
        }

        public Criteria andAttrTypeIn(List<String> values) {
            addCriterion("ATTR_TYPE in", values, "attrType");
            return (Criteria) this;
        }

        public Criteria andAttrTypeNotIn(List<String> values) {
            addCriterion("ATTR_TYPE not in", values, "attrType");
            return (Criteria) this;
        }

        public Criteria andAttrTypeBetween(String value1, String value2) {
            addCriterion("ATTR_TYPE between", value1, value2, "attrType");
            return (Criteria) this;
        }

        public Criteria andAttrTypeNotBetween(String value1, String value2) {
            addCriterion("ATTR_TYPE not between", value1, value2, "attrType");
            return (Criteria) this;
        }

        public Criteria andIsSettlementIsNull() {
            addCriterion("IS_SETTLEMENT is null");
            return (Criteria) this;
        }

        public Criteria andIsSettlementIsNotNull() {
            addCriterion("IS_SETTLEMENT is not null");
            return (Criteria) this;
        }

        public Criteria andIsSettlementEqualTo(String value) {
            addCriterion("IS_SETTLEMENT =", value, "isSettlement");
            return (Criteria) this;
        }

        public Criteria andIsSettlementNotEqualTo(String value) {
            addCriterion("IS_SETTLEMENT <>", value, "isSettlement");
            return (Criteria) this;
        }

        public Criteria andIsSettlementGreaterThan(String value) {
            addCriterion("IS_SETTLEMENT >", value, "isSettlement");
            return (Criteria) this;
        }

        public Criteria andIsSettlementGreaterThanOrEqualTo(String value) {
            addCriterion("IS_SETTLEMENT >=", value, "isSettlement");
            return (Criteria) this;
        }

        public Criteria andIsSettlementLessThan(String value) {
            addCriterion("IS_SETTLEMENT <", value, "isSettlement");
            return (Criteria) this;
        }

        public Criteria andIsSettlementLessThanOrEqualTo(String value) {
            addCriterion("IS_SETTLEMENT <=", value, "isSettlement");
            return (Criteria) this;
        }

        public Criteria andIsSettlementLike(String value) {
            addCriterion("IS_SETTLEMENT like", value, "isSettlement");
            return (Criteria) this;
        }

        public Criteria andIsSettlementNotLike(String value) {
            addCriterion("IS_SETTLEMENT not like", value, "isSettlement");
            return (Criteria) this;
        }

        public Criteria andIsSettlementIn(List<String> values) {
            addCriterion("IS_SETTLEMENT in", values, "isSettlement");
            return (Criteria) this;
        }

        public Criteria andIsSettlementNotIn(List<String> values) {
            addCriterion("IS_SETTLEMENT not in", values, "isSettlement");
            return (Criteria) this;
        }

        public Criteria andIsSettlementBetween(String value1, String value2) {
            addCriterion("IS_SETTLEMENT between", value1, value2, "isSettlement");
            return (Criteria) this;
        }

        public Criteria andIsSettlementNotBetween(String value1, String value2) {
            addCriterion("IS_SETTLEMENT not between", value1, value2, "isSettlement");
            return (Criteria) this;
        }

        public Criteria andValueTypeIsNull() {
            addCriterion("VALUE_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andValueTypeIsNotNull() {
            addCriterion("VALUE_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andValueTypeEqualTo(String value) {
            addCriterion("VALUE_TYPE =", value, "valueType");
            return (Criteria) this;
        }

        public Criteria andValueTypeNotEqualTo(String value) {
            addCriterion("VALUE_TYPE <>", value, "valueType");
            return (Criteria) this;
        }

        public Criteria andValueTypeGreaterThan(String value) {
            addCriterion("VALUE_TYPE >", value, "valueType");
            return (Criteria) this;
        }

        public Criteria andValueTypeGreaterThanOrEqualTo(String value) {
            addCriterion("VALUE_TYPE >=", value, "valueType");
            return (Criteria) this;
        }

        public Criteria andValueTypeLessThan(String value) {
            addCriterion("VALUE_TYPE <", value, "valueType");
            return (Criteria) this;
        }

        public Criteria andValueTypeLessThanOrEqualTo(String value) {
            addCriterion("VALUE_TYPE <=", value, "valueType");
            return (Criteria) this;
        }

        public Criteria andValueTypeLike(String value) {
            addCriterion("VALUE_TYPE like", value, "valueType");
            return (Criteria) this;
        }

        public Criteria andValueTypeNotLike(String value) {
            addCriterion("VALUE_TYPE not like", value, "valueType");
            return (Criteria) this;
        }

        public Criteria andValueTypeIn(List<String> values) {
            addCriterion("VALUE_TYPE in", values, "valueType");
            return (Criteria) this;
        }

        public Criteria andValueTypeNotIn(List<String> values) {
            addCriterion("VALUE_TYPE not in", values, "valueType");
            return (Criteria) this;
        }

        public Criteria andValueTypeBetween(String value1, String value2) {
            addCriterion("VALUE_TYPE between", value1, value2, "valueType");
            return (Criteria) this;
        }

        public Criteria andValueTypeNotBetween(String value1, String value2) {
            addCriterion("VALUE_TYPE not between", value1, value2, "valueType");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryIsNull() {
            addCriterion("IS_NECESSARY is null");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryIsNotNull() {
            addCriterion("IS_NECESSARY is not null");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryEqualTo(String value) {
            addCriterion("IS_NECESSARY =", value, "isNecessary");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryNotEqualTo(String value) {
            addCriterion("IS_NECESSARY <>", value, "isNecessary");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryGreaterThan(String value) {
            addCriterion("IS_NECESSARY >", value, "isNecessary");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryGreaterThanOrEqualTo(String value) {
            addCriterion("IS_NECESSARY >=", value, "isNecessary");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryLessThan(String value) {
            addCriterion("IS_NECESSARY <", value, "isNecessary");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryLessThanOrEqualTo(String value) {
            addCriterion("IS_NECESSARY <=", value, "isNecessary");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryLike(String value) {
            addCriterion("IS_NECESSARY like", value, "isNecessary");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryNotLike(String value) {
            addCriterion("IS_NECESSARY not like", value, "isNecessary");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryIn(List<String> values) {
            addCriterion("IS_NECESSARY in", values, "isNecessary");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryNotIn(List<String> values) {
            addCriterion("IS_NECESSARY not in", values, "isNecessary");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryBetween(String value1, String value2) {
            addCriterion("IS_NECESSARY between", value1, value2, "isNecessary");
            return (Criteria) this;
        }

        public Criteria andIsNecessaryNotBetween(String value1, String value2) {
            addCriterion("IS_NECESSARY not between", value1, value2, "isNecessary");
            return (Criteria) this;
        }

        public Criteria andElementTypeIsNull() {
            addCriterion("ELEMENT_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andElementTypeIsNotNull() {
            addCriterion("ELEMENT_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andElementTypeEqualTo(String value) {
            addCriterion("ELEMENT_TYPE =", value, "elementType");
            return (Criteria) this;
        }

        public Criteria andElementTypeNotEqualTo(String value) {
            addCriterion("ELEMENT_TYPE <>", value, "elementType");
            return (Criteria) this;
        }

        public Criteria andElementTypeGreaterThan(String value) {
            addCriterion("ELEMENT_TYPE >", value, "elementType");
            return (Criteria) this;
        }

        public Criteria andElementTypeGreaterThanOrEqualTo(String value) {
            addCriterion("ELEMENT_TYPE >=", value, "elementType");
            return (Criteria) this;
        }

        public Criteria andElementTypeLessThan(String value) {
            addCriterion("ELEMENT_TYPE <", value, "elementType");
            return (Criteria) this;
        }

        public Criteria andElementTypeLessThanOrEqualTo(String value) {
            addCriterion("ELEMENT_TYPE <=", value, "elementType");
            return (Criteria) this;
        }

        public Criteria andElementTypeLike(String value) {
            addCriterion("ELEMENT_TYPE like", value, "elementType");
            return (Criteria) this;
        }

        public Criteria andElementTypeNotLike(String value) {
            addCriterion("ELEMENT_TYPE not like", value, "elementType");
            return (Criteria) this;
        }

        public Criteria andElementTypeIn(List<String> values) {
            addCriterion("ELEMENT_TYPE in", values, "elementType");
            return (Criteria) this;
        }

        public Criteria andElementTypeNotIn(List<String> values) {
            addCriterion("ELEMENT_TYPE not in", values, "elementType");
            return (Criteria) this;
        }

        public Criteria andElementTypeBetween(String value1, String value2) {
            addCriterion("ELEMENT_TYPE between", value1, value2, "elementType");
            return (Criteria) this;
        }

        public Criteria andElementTypeNotBetween(String value1, String value2) {
            addCriterion("ELEMENT_TYPE not between", value1, value2, "elementType");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeIsNull() {
            addCriterion("DATA_CREATE_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeIsNotNull() {
            addCriterion("DATA_CREATE_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeEqualTo(String value) {
            addCriterion("DATA_CREATE_TYPE =", value, "dataCreateType");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeNotEqualTo(String value) {
            addCriterion("DATA_CREATE_TYPE <>", value, "dataCreateType");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeGreaterThan(String value) {
            addCriterion("DATA_CREATE_TYPE >", value, "dataCreateType");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeGreaterThanOrEqualTo(String value) {
            addCriterion("DATA_CREATE_TYPE >=", value, "dataCreateType");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeLessThan(String value) {
            addCriterion("DATA_CREATE_TYPE <", value, "dataCreateType");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeLessThanOrEqualTo(String value) {
            addCriterion("DATA_CREATE_TYPE <=", value, "dataCreateType");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeLike(String value) {
            addCriterion("DATA_CREATE_TYPE like", value, "dataCreateType");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeNotLike(String value) {
            addCriterion("DATA_CREATE_TYPE not like", value, "dataCreateType");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeIn(List<String> values) {
            addCriterion("DATA_CREATE_TYPE in", values, "dataCreateType");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeNotIn(List<String> values) {
            addCriterion("DATA_CREATE_TYPE not in", values, "dataCreateType");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeBetween(String value1, String value2) {
            addCriterion("DATA_CREATE_TYPE between", value1, value2, "dataCreateType");
            return (Criteria) this;
        }

        public Criteria andDataCreateTypeNotBetween(String value1, String value2) {
            addCriterion("DATA_CREATE_TYPE not between", value1, value2, "dataCreateType");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyIsNull() {
            addCriterion("IS_PRIMARY_KEY is null");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyIsNotNull() {
            addCriterion("IS_PRIMARY_KEY is not null");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyEqualTo(String value) {
            addCriterion("IS_PRIMARY_KEY =", value, "isPrimaryKey");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyNotEqualTo(String value) {
            addCriterion("IS_PRIMARY_KEY <>", value, "isPrimaryKey");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyGreaterThan(String value) {
            addCriterion("IS_PRIMARY_KEY >", value, "isPrimaryKey");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyGreaterThanOrEqualTo(String value) {
            addCriterion("IS_PRIMARY_KEY >=", value, "isPrimaryKey");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyLessThan(String value) {
            addCriterion("IS_PRIMARY_KEY <", value, "isPrimaryKey");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyLessThanOrEqualTo(String value) {
            addCriterion("IS_PRIMARY_KEY <=", value, "isPrimaryKey");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyLike(String value) {
            addCriterion("IS_PRIMARY_KEY like", value, "isPrimaryKey");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyNotLike(String value) {
            addCriterion("IS_PRIMARY_KEY not like", value, "isPrimaryKey");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyIn(List<String> values) {
            addCriterion("IS_PRIMARY_KEY in", values, "isPrimaryKey");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyNotIn(List<String> values) {
            addCriterion("IS_PRIMARY_KEY not in", values, "isPrimaryKey");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyBetween(String value1, String value2) {
            addCriterion("IS_PRIMARY_KEY between", value1, value2, "isPrimaryKey");
            return (Criteria) this;
        }

        public Criteria andIsPrimaryKeyNotBetween(String value1, String value2) {
            addCriterion("IS_PRIMARY_KEY not between", value1, value2, "isPrimaryKey");
            return (Criteria) this;
        }

        public Criteria andUnitIsNull() {
            addCriterion("UNIT is null");
            return (Criteria) this;
        }

        public Criteria andUnitIsNotNull() {
            addCriterion("UNIT is not null");
            return (Criteria) this;
        }

        public Criteria andUnitEqualTo(String value) {
            addCriterion("UNIT =", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotEqualTo(String value) {
            addCriterion("UNIT <>", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitGreaterThan(String value) {
            addCriterion("UNIT >", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitGreaterThanOrEqualTo(String value) {
            addCriterion("UNIT >=", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitLessThan(String value) {
            addCriterion("UNIT <", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitLessThanOrEqualTo(String value) {
            addCriterion("UNIT <=", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitLike(String value) {
            addCriterion("UNIT like", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotLike(String value) {
            addCriterion("UNIT not like", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitIn(List<String> values) {
            addCriterion("UNIT in", values, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotIn(List<String> values) {
            addCriterion("UNIT not in", values, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitBetween(String value1, String value2) {
            addCriterion("UNIT between", value1, value2, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotBetween(String value1, String value2) {
            addCriterion("UNIT not between", value1, value2, "unit");
            return (Criteria) this;
        }

        public Criteria andSortIdIsNull() {
            addCriterion("SORT_ID is null");
            return (Criteria) this;
        }

        public Criteria andSortIdIsNotNull() {
            addCriterion("SORT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSortIdEqualTo(Integer value) {
            addCriterion("SORT_ID =", value, "sortId");
            return (Criteria) this;
        }

        public Criteria andSortIdNotEqualTo(Integer value) {
            addCriterion("SORT_ID <>", value, "sortId");
            return (Criteria) this;
        }

        public Criteria andSortIdGreaterThan(Integer value) {
            addCriterion("SORT_ID >", value, "sortId");
            return (Criteria) this;
        }

        public Criteria andSortIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("SORT_ID >=", value, "sortId");
            return (Criteria) this;
        }

        public Criteria andSortIdLessThan(Integer value) {
            addCriterion("SORT_ID <", value, "sortId");
            return (Criteria) this;
        }

        public Criteria andSortIdLessThanOrEqualTo(Integer value) {
            addCriterion("SORT_ID <=", value, "sortId");
            return (Criteria) this;
        }

        public Criteria andSortIdIn(List<Integer> values) {
            addCriterion("SORT_ID in", values, "sortId");
            return (Criteria) this;
        }

        public Criteria andSortIdNotIn(List<Integer> values) {
            addCriterion("SORT_ID not in", values, "sortId");
            return (Criteria) this;
        }

        public Criteria andSortIdBetween(Integer value1, Integer value2) {
            addCriterion("SORT_ID between", value1, value2, "sortId");
            return (Criteria) this;
        }

        public Criteria andSortIdNotBetween(Integer value1, Integer value2) {
            addCriterion("SORT_ID not between", value1, value2, "sortId");
            return (Criteria) this;
        }

        public Criteria andElementDescIsNull() {
            addCriterion("ELEMENT_DESC is null");
            return (Criteria) this;
        }

        public Criteria andElementDescIsNotNull() {
            addCriterion("ELEMENT_DESC is not null");
            return (Criteria) this;
        }

        public Criteria andElementDescEqualTo(String value) {
            addCriterion("ELEMENT_DESC =", value, "elementDesc");
            return (Criteria) this;
        }

        public Criteria andElementDescNotEqualTo(String value) {
            addCriterion("ELEMENT_DESC <>", value, "elementDesc");
            return (Criteria) this;
        }

        public Criteria andElementDescGreaterThan(String value) {
            addCriterion("ELEMENT_DESC >", value, "elementDesc");
            return (Criteria) this;
        }

        public Criteria andElementDescGreaterThanOrEqualTo(String value) {
            addCriterion("ELEMENT_DESC >=", value, "elementDesc");
            return (Criteria) this;
        }

        public Criteria andElementDescLessThan(String value) {
            addCriterion("ELEMENT_DESC <", value, "elementDesc");
            return (Criteria) this;
        }

        public Criteria andElementDescLessThanOrEqualTo(String value) {
            addCriterion("ELEMENT_DESC <=", value, "elementDesc");
            return (Criteria) this;
        }

        public Criteria andElementDescLike(String value) {
            addCriterion("ELEMENT_DESC like", value, "elementDesc");
            return (Criteria) this;
        }

        public Criteria andElementDescNotLike(String value) {
            addCriterion("ELEMENT_DESC not like", value, "elementDesc");
            return (Criteria) this;
        }

        public Criteria andElementDescIn(List<String> values) {
            addCriterion("ELEMENT_DESC in", values, "elementDesc");
            return (Criteria) this;
        }

        public Criteria andElementDescNotIn(List<String> values) {
            addCriterion("ELEMENT_DESC not in", values, "elementDesc");
            return (Criteria) this;
        }

        public Criteria andElementDescBetween(String value1, String value2) {
            addCriterion("ELEMENT_DESC between", value1, value2, "elementDesc");
            return (Criteria) this;
        }

        public Criteria andElementDescNotBetween(String value1, String value2) {
            addCriterion("ELEMENT_DESC not between", value1, value2, "elementDesc");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeIsNull() {
            addCriterion("STATISTICS_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeIsNotNull() {
            addCriterion("STATISTICS_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeEqualTo(String value) {
            addCriterion("STATISTICS_TYPE =", value, "statisticsType");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeNotEqualTo(String value) {
            addCriterion("STATISTICS_TYPE <>", value, "statisticsType");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeGreaterThan(String value) {
            addCriterion("STATISTICS_TYPE >", value, "statisticsType");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeGreaterThanOrEqualTo(String value) {
            addCriterion("STATISTICS_TYPE >=", value, "statisticsType");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeLessThan(String value) {
            addCriterion("STATISTICS_TYPE <", value, "statisticsType");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeLessThanOrEqualTo(String value) {
            addCriterion("STATISTICS_TYPE <=", value, "statisticsType");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeLike(String value) {
            addCriterion("STATISTICS_TYPE like", value, "statisticsType");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeNotLike(String value) {
            addCriterion("STATISTICS_TYPE not like", value, "statisticsType");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeIn(List<String> values) {
            addCriterion("STATISTICS_TYPE in", values, "statisticsType");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeNotIn(List<String> values) {
            addCriterion("STATISTICS_TYPE not in", values, "statisticsType");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeBetween(String value1, String value2) {
            addCriterion("STATISTICS_TYPE between", value1, value2, "statisticsType");
            return (Criteria) this;
        }

        public Criteria andStatisticsTypeNotBetween(String value1, String value2) {
            addCriterion("STATISTICS_TYPE not between", value1, value2, "statisticsType");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycIsNull() {
            addCriterion("STATISTICS_CYC is null");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycIsNotNull() {
            addCriterion("STATISTICS_CYC is not null");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycEqualTo(String value) {
            addCriterion("STATISTICS_CYC =", value, "statisticsCyc");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycNotEqualTo(String value) {
            addCriterion("STATISTICS_CYC <>", value, "statisticsCyc");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycGreaterThan(String value) {
            addCriterion("STATISTICS_CYC >", value, "statisticsCyc");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycGreaterThanOrEqualTo(String value) {
            addCriterion("STATISTICS_CYC >=", value, "statisticsCyc");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycLessThan(String value) {
            addCriterion("STATISTICS_CYC <", value, "statisticsCyc");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycLessThanOrEqualTo(String value) {
            addCriterion("STATISTICS_CYC <=", value, "statisticsCyc");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycLike(String value) {
            addCriterion("STATISTICS_CYC like", value, "statisticsCyc");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycNotLike(String value) {
            addCriterion("STATISTICS_CYC not like", value, "statisticsCyc");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycIn(List<String> values) {
            addCriterion("STATISTICS_CYC in", values, "statisticsCyc");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycNotIn(List<String> values) {
            addCriterion("STATISTICS_CYC not in", values, "statisticsCyc");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycBetween(String value1, String value2) {
            addCriterion("STATISTICS_CYC between", value1, value2, "statisticsCyc");
            return (Criteria) this;
        }

        public Criteria andStatisticsCycNotBetween(String value1, String value2) {
            addCriterion("STATISTICS_CYC not between", value1, value2, "statisticsCyc");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdIsNull() {
            addCriterion("STATISTICS_OBJECT_ID is null");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdIsNotNull() {
            addCriterion("STATISTICS_OBJECT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdEqualTo(String value) {
            addCriterion("STATISTICS_OBJECT_ID =", value, "statisticsObjectId");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdNotEqualTo(String value) {
            addCriterion("STATISTICS_OBJECT_ID <>", value, "statisticsObjectId");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdGreaterThan(String value) {
            addCriterion("STATISTICS_OBJECT_ID >", value, "statisticsObjectId");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdGreaterThanOrEqualTo(String value) {
            addCriterion("STATISTICS_OBJECT_ID >=", value, "statisticsObjectId");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdLessThan(String value) {
            addCriterion("STATISTICS_OBJECT_ID <", value, "statisticsObjectId");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdLessThanOrEqualTo(String value) {
            addCriterion("STATISTICS_OBJECT_ID <=", value, "statisticsObjectId");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdLike(String value) {
            addCriterion("STATISTICS_OBJECT_ID like", value, "statisticsObjectId");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdNotLike(String value) {
            addCriterion("STATISTICS_OBJECT_ID not like", value, "statisticsObjectId");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdIn(List<String> values) {
            addCriterion("STATISTICS_OBJECT_ID in", values, "statisticsObjectId");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdNotIn(List<String> values) {
            addCriterion("STATISTICS_OBJECT_ID not in", values, "statisticsObjectId");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdBetween(String value1, String value2) {
            addCriterion("STATISTICS_OBJECT_ID between", value1, value2, "statisticsObjectId");
            return (Criteria) this;
        }

        public Criteria andStatisticsObjectIdNotBetween(String value1, String value2) {
            addCriterion("STATISTICS_OBJECT_ID not between", value1, value2, "statisticsObjectId");
            return (Criteria) this;
        }

        public Criteria andStatisticsElementIdIsNull() {
            addCriterion("STATISTICS_ELEMENT_ID is null");
            return (Criteria) this;
        }

        public Criteria andStatisticsElementIdIsNotNull() {
            addCriterion("STATISTICS_ELEMENT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andStatisticsElementIdEqualTo(Long value) {
            addCriterion("STATISTICS_ELEMENT_ID =", value, "statisticsElementId");
            return (Criteria) this;
        }

        public Criteria andStatisticsElementIdNotEqualTo(Long value) {
            addCriterion("STATISTICS_ELEMENT_ID <>", value, "statisticsElementId");
            return (Criteria) this;
        }

        public Criteria andStatisticsElementIdGreaterThan(Long value) {
            addCriterion("STATISTICS_ELEMENT_ID >", value, "statisticsElementId");
            return (Criteria) this;
        }

        public Criteria andStatisticsElementIdGreaterThanOrEqualTo(Long value) {
            addCriterion("STATISTICS_ELEMENT_ID >=", value, "statisticsElementId");
            return (Criteria) this;
        }

        public Criteria andStatisticsElementIdLessThan(Long value) {
            addCriterion("STATISTICS_ELEMENT_ID <", value, "statisticsElementId");
            return (Criteria) this;
        }

        public Criteria andStatisticsElementIdLessThanOrEqualTo(Long value) {
            addCriterion("STATISTICS_ELEMENT_ID <=", value, "statisticsElementId");
            return (Criteria) this;
        }

        public Criteria andStatisticsElementIdIn(List<Long> values) {
            addCriterion("STATISTICS_ELEMENT_ID in", values, "statisticsElementId");
            return (Criteria) this;
        }

        public Criteria andStatisticsElementIdNotIn(List<Long> values) {
            addCriterion("STATISTICS_ELEMENT_ID not in", values, "statisticsElementId");
            return (Criteria) this;
        }

        public Criteria andStatisticsElementIdBetween(Long value1, Long value2) {
            addCriterion("STATISTICS_ELEMENT_ID between", value1, value2, "statisticsElementId");
            return (Criteria) this;
        }

        public Criteria andStatisticsElementIdNotBetween(Long value1, Long value2) {
            addCriterion("STATISTICS_ELEMENT_ID not between", value1, value2, "statisticsElementId");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("STATE is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("STATE is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(String value) {
            addCriterion("STATE =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(String value) {
            addCriterion("STATE <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(String value) {
            addCriterion("STATE >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(String value) {
            addCriterion("STATE >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(String value) {
            addCriterion("STATE <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(String value) {
            addCriterion("STATE <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLike(String value) {
            addCriterion("STATE like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotLike(String value) {
            addCriterion("STATE not like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<String> values) {
            addCriterion("STATE in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<String> values) {
            addCriterion("STATE not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(String value1, String value2) {
            addCriterion("STATE between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(String value1, String value2) {
            addCriterion("STATE not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdIsNull() {
            addCriterion("CREATE_DEPT_ID is null");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdIsNotNull() {
            addCriterion("CREATE_DEPT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdEqualTo(String value) {
            addCriterion("CREATE_DEPT_ID =", value, "createDeptId");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdNotEqualTo(String value) {
            addCriterion("CREATE_DEPT_ID <>", value, "createDeptId");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdGreaterThan(String value) {
            addCriterion("CREATE_DEPT_ID >", value, "createDeptId");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdGreaterThanOrEqualTo(String value) {
            addCriterion("CREATE_DEPT_ID >=", value, "createDeptId");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdLessThan(String value) {
            addCriterion("CREATE_DEPT_ID <", value, "createDeptId");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdLessThanOrEqualTo(String value) {
            addCriterion("CREATE_DEPT_ID <=", value, "createDeptId");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdLike(String value) {
            addCriterion("CREATE_DEPT_ID like", value, "createDeptId");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdNotLike(String value) {
            addCriterion("CREATE_DEPT_ID not like", value, "createDeptId");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdIn(List<String> values) {
            addCriterion("CREATE_DEPT_ID in", values, "createDeptId");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdNotIn(List<String> values) {
            addCriterion("CREATE_DEPT_ID not in", values, "createDeptId");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdBetween(String value1, String value2) {
            addCriterion("CREATE_DEPT_ID between", value1, value2, "createDeptId");
            return (Criteria) this;
        }

        public Criteria andCreateDeptIdNotBetween(String value1, String value2) {
            addCriterion("CREATE_DEPT_ID not between", value1, value2, "createDeptId");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdIsNull() {
            addCriterion("CREATE_OPER_ID is null");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdIsNotNull() {
            addCriterion("CREATE_OPER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdEqualTo(String value) {
            addCriterion("CREATE_OPER_ID =", value, "createOperId");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdNotEqualTo(String value) {
            addCriterion("CREATE_OPER_ID <>", value, "createOperId");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdGreaterThan(String value) {
            addCriterion("CREATE_OPER_ID >", value, "createOperId");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdGreaterThanOrEqualTo(String value) {
            addCriterion("CREATE_OPER_ID >=", value, "createOperId");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdLessThan(String value) {
            addCriterion("CREATE_OPER_ID <", value, "createOperId");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdLessThanOrEqualTo(String value) {
            addCriterion("CREATE_OPER_ID <=", value, "createOperId");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdLike(String value) {
            addCriterion("CREATE_OPER_ID like", value, "createOperId");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdNotLike(String value) {
            addCriterion("CREATE_OPER_ID not like", value, "createOperId");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdIn(List<String> values) {
            addCriterion("CREATE_OPER_ID in", values, "createOperId");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdNotIn(List<String> values) {
            addCriterion("CREATE_OPER_ID not in", values, "createOperId");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdBetween(String value1, String value2) {
            addCriterion("CREATE_OPER_ID between", value1, value2, "createOperId");
            return (Criteria) this;
        }

        public Criteria andCreateOperIdNotBetween(String value1, String value2) {
            addCriterion("CREATE_OPER_ID not between", value1, value2, "createOperId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("CREATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("CREATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Timestamp value) {
            addCriterion("CREATE_TIME =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Timestamp value) {
            addCriterion("CREATE_TIME <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Timestamp value) {
            addCriterion("CREATE_TIME >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("CREATE_TIME >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Timestamp value) {
            addCriterion("CREATE_TIME <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("CREATE_TIME <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Timestamp> values) {
            addCriterion("CREATE_TIME in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Timestamp> values) {
            addCriterion("CREATE_TIME not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("CREATE_TIME between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("CREATE_TIME not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdIsNull() {
            addCriterion("UPDATE_DEPT_ID is null");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdIsNotNull() {
            addCriterion("UPDATE_DEPT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdEqualTo(String value) {
            addCriterion("UPDATE_DEPT_ID =", value, "updateDeptId");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdNotEqualTo(String value) {
            addCriterion("UPDATE_DEPT_ID <>", value, "updateDeptId");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdGreaterThan(String value) {
            addCriterion("UPDATE_DEPT_ID >", value, "updateDeptId");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdGreaterThanOrEqualTo(String value) {
            addCriterion("UPDATE_DEPT_ID >=", value, "updateDeptId");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdLessThan(String value) {
            addCriterion("UPDATE_DEPT_ID <", value, "updateDeptId");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdLessThanOrEqualTo(String value) {
            addCriterion("UPDATE_DEPT_ID <=", value, "updateDeptId");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdLike(String value) {
            addCriterion("UPDATE_DEPT_ID like", value, "updateDeptId");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdNotLike(String value) {
            addCriterion("UPDATE_DEPT_ID not like", value, "updateDeptId");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdIn(List<String> values) {
            addCriterion("UPDATE_DEPT_ID in", values, "updateDeptId");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdNotIn(List<String> values) {
            addCriterion("UPDATE_DEPT_ID not in", values, "updateDeptId");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdBetween(String value1, String value2) {
            addCriterion("UPDATE_DEPT_ID between", value1, value2, "updateDeptId");
            return (Criteria) this;
        }

        public Criteria andUpdateDeptIdNotBetween(String value1, String value2) {
            addCriterion("UPDATE_DEPT_ID not between", value1, value2, "updateDeptId");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdIsNull() {
            addCriterion("UPDATE_OPER_ID is null");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdIsNotNull() {
            addCriterion("UPDATE_OPER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdEqualTo(String value) {
            addCriterion("UPDATE_OPER_ID =", value, "updateOperId");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdNotEqualTo(String value) {
            addCriterion("UPDATE_OPER_ID <>", value, "updateOperId");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdGreaterThan(String value) {
            addCriterion("UPDATE_OPER_ID >", value, "updateOperId");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdGreaterThanOrEqualTo(String value) {
            addCriterion("UPDATE_OPER_ID >=", value, "updateOperId");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdLessThan(String value) {
            addCriterion("UPDATE_OPER_ID <", value, "updateOperId");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdLessThanOrEqualTo(String value) {
            addCriterion("UPDATE_OPER_ID <=", value, "updateOperId");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdLike(String value) {
            addCriterion("UPDATE_OPER_ID like", value, "updateOperId");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdNotLike(String value) {
            addCriterion("UPDATE_OPER_ID not like", value, "updateOperId");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdIn(List<String> values) {
            addCriterion("UPDATE_OPER_ID in", values, "updateOperId");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdNotIn(List<String> values) {
            addCriterion("UPDATE_OPER_ID not in", values, "updateOperId");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdBetween(String value1, String value2) {
            addCriterion("UPDATE_OPER_ID between", value1, value2, "updateOperId");
            return (Criteria) this;
        }

        public Criteria andUpdateOperIdNotBetween(String value1, String value2) {
            addCriterion("UPDATE_OPER_ID not between", value1, value2, "updateOperId");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("UPDATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("UPDATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Timestamp value) {
            addCriterion("UPDATE_TIME =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Timestamp value) {
            addCriterion("UPDATE_TIME <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Timestamp value) {
            addCriterion("UPDATE_TIME >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("UPDATE_TIME >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Timestamp value) {
            addCriterion("UPDATE_TIME <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("UPDATE_TIME <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Timestamp> values) {
            addCriterion("UPDATE_TIME in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Timestamp> values) {
            addCriterion("UPDATE_TIME not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("UPDATE_TIME between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("UPDATE_TIME not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}