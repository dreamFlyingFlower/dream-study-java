package com.wy.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * mybatis条件查询基类,未完成 FIXME
 * 
 * @author ParadiseWY
 * @date 2020年4月6日 上午8:51:31
 */
public abstract class AbstractExample<T> {

	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public AbstractExample() {
		oredCriteria = new ArrayList<>();
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

	protected abstract static class GeneratedCriteria {

		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<>();
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

		public Criteria andConfigIdIsNull() {
			addCriterion("config_id is null");
			return (Criteria) this;
		}

		public Criteria andConfigIdIsNotNull() {
			addCriterion("config_id is not null");
			return (Criteria) this;
		}

		public Criteria andConfigIdEqualTo(Integer value) {
			addCriterion("config_id =", value, "configId");
			return (Criteria) this;
		}

		public Criteria andConfigIdNotEqualTo(Integer value) {
			addCriterion("config_id <>", value, "configId");
			return (Criteria) this;
		}

		public Criteria andConfigIdGreaterThan(Integer value) {
			addCriterion("config_id >", value, "configId");
			return (Criteria) this;
		}

		public Criteria andConfigIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("config_id >=", value, "configId");
			return (Criteria) this;
		}

		public Criteria andConfigIdLessThan(Integer value) {
			addCriterion("config_id <", value, "configId");
			return (Criteria) this;
		}

		public Criteria andConfigIdLessThanOrEqualTo(Integer value) {
			addCriterion("config_id <=", value, "configId");
			return (Criteria) this;
		}

		public Criteria andConfigIdIn(List<Integer> values) {
			addCriterion("config_id in", values, "configId");
			return (Criteria) this;
		}

		public Criteria andConfigIdNotIn(List<Integer> values) {
			addCriterion("config_id not in", values, "configId");
			return (Criteria) this;
		}

		public Criteria andConfigIdBetween(Integer value1, Integer value2) {
			addCriterion("config_id between", value1, value2, "configId");
			return (Criteria) this;
		}

		public Criteria andConfigIdNotBetween(Integer value1, Integer value2) {
			addCriterion("config_id not between", value1, value2, "configId");
			return (Criteria) this;
		}

		public Criteria andConfigNameIsNull() {
			addCriterion("config_name is null");
			return (Criteria) this;
		}

		public Criteria andConfigNameIsNotNull() {
			addCriterion("config_name is not null");
			return (Criteria) this;
		}

		public Criteria andConfigNameEqualTo(String value) {
			addCriterion("config_name =", value, "configName");
			return (Criteria) this;
		}

		public Criteria andConfigNameNotEqualTo(String value) {
			addCriterion("config_name <>", value, "configName");
			return (Criteria) this;
		}

		public Criteria andConfigNameGreaterThan(String value) {
			addCriterion("config_name >", value, "configName");
			return (Criteria) this;
		}

		public Criteria andConfigNameGreaterThanOrEqualTo(String value) {
			addCriterion("config_name >=", value, "configName");
			return (Criteria) this;
		}

		public Criteria andConfigNameLessThan(String value) {
			addCriterion("config_name <", value, "configName");
			return (Criteria) this;
		}

		public Criteria andConfigNameLessThanOrEqualTo(String value) {
			addCriterion("config_name <=", value, "configName");
			return (Criteria) this;
		}

		public Criteria andConfigNameLike(String value) {
			addCriterion("config_name like", value, "configName");
			return (Criteria) this;
		}

		public Criteria andConfigNameNotLike(String value) {
			addCriterion("config_name not like", value, "configName");
			return (Criteria) this;
		}

		public Criteria andConfigNameIn(List<String> values) {
			addCriterion("config_name in", values, "configName");
			return (Criteria) this;
		}

		public Criteria andConfigNameNotIn(List<String> values) {
			addCriterion("config_name not in", values, "configName");
			return (Criteria) this;
		}

		public Criteria andConfigNameBetween(String value1, String value2) {
			addCriterion("config_name between", value1, value2, "configName");
			return (Criteria) this;
		}

		public Criteria andConfigNameNotBetween(String value1, String value2) {
			addCriterion("config_name not between", value1, value2, "configName");
			return (Criteria) this;
		}

		public Criteria andConfigKeyIsNull() {
			addCriterion("config_key is null");
			return (Criteria) this;
		}

		public Criteria andConfigKeyIsNotNull() {
			addCriterion("config_key is not null");
			return (Criteria) this;
		}

		public Criteria andConfigKeyEqualTo(String value) {
			addCriterion("config_key =", value, "configKey");
			return (Criteria) this;
		}

		public Criteria andConfigKeyNotEqualTo(String value) {
			addCriterion("config_key <>", value, "configKey");
			return (Criteria) this;
		}

		public Criteria andConfigKeyGreaterThan(String value) {
			addCriterion("config_key >", value, "configKey");
			return (Criteria) this;
		}

		public Criteria andConfigKeyGreaterThanOrEqualTo(String value) {
			addCriterion("config_key >=", value, "configKey");
			return (Criteria) this;
		}

		public Criteria andConfigKeyLessThan(String value) {
			addCriterion("config_key <", value, "configKey");
			return (Criteria) this;
		}

		public Criteria andConfigKeyLessThanOrEqualTo(String value) {
			addCriterion("config_key <=", value, "configKey");
			return (Criteria) this;
		}

		public Criteria andConfigKeyLike(String value) {
			addCriterion("config_key like", value, "configKey");
			return (Criteria) this;
		}

		public Criteria andConfigKeyNotLike(String value) {
			addCriterion("config_key not like", value, "configKey");
			return (Criteria) this;
		}

		public Criteria andConfigKeyIn(List<String> values) {
			addCriterion("config_key in", values, "configKey");
			return (Criteria) this;
		}

		public Criteria andConfigKeyNotIn(List<String> values) {
			addCriterion("config_key not in", values, "configKey");
			return (Criteria) this;
		}

		public Criteria andConfigKeyBetween(String value1, String value2) {
			addCriterion("config_key between", value1, value2, "configKey");
			return (Criteria) this;
		}

		public Criteria andConfigKeyNotBetween(String value1, String value2) {
			addCriterion("config_key not between", value1, value2, "configKey");
			return (Criteria) this;
		}

		public Criteria andConfigValueIsNull() {
			addCriterion("config_value is null");
			return (Criteria) this;
		}

		public Criteria andConfigValueIsNotNull() {
			addCriterion("config_value is not null");
			return (Criteria) this;
		}

		public Criteria andConfigValueEqualTo(String value) {
			addCriterion("config_value =", value, "configValue");
			return (Criteria) this;
		}

		public Criteria andConfigValueNotEqualTo(String value) {
			addCriterion("config_value <>", value, "configValue");
			return (Criteria) this;
		}

		public Criteria andConfigValueGreaterThan(String value) {
			addCriterion("config_value >", value, "configValue");
			return (Criteria) this;
		}

		public Criteria andConfigValueGreaterThanOrEqualTo(String value) {
			addCriterion("config_value >=", value, "configValue");
			return (Criteria) this;
		}

		public Criteria andConfigValueLessThan(String value) {
			addCriterion("config_value <", value, "configValue");
			return (Criteria) this;
		}

		public Criteria andConfigValueLessThanOrEqualTo(String value) {
			addCriterion("config_value <=", value, "configValue");
			return (Criteria) this;
		}

		public Criteria andConfigValueLike(String value) {
			addCriterion("config_value like", value, "configValue");
			return (Criteria) this;
		}

		public Criteria andConfigValueNotLike(String value) {
			addCriterion("config_value not like", value, "configValue");
			return (Criteria) this;
		}

		public Criteria andConfigValueIn(List<String> values) {
			addCriterion("config_value in", values, "configValue");
			return (Criteria) this;
		}

		public Criteria andConfigValueNotIn(List<String> values) {
			addCriterion("config_value not in", values, "configValue");
			return (Criteria) this;
		}

		public Criteria andConfigValueBetween(String value1, String value2) {
			addCriterion("config_value between", value1, value2, "configValue");
			return (Criteria) this;
		}

		public Criteria andConfigValueNotBetween(String value1, String value2) {
			addCriterion("config_value not between", value1, value2, "configValue");
			return (Criteria) this;
		}

		public Criteria andConfigTypeIsNull() {
			addCriterion("config_type is null");
			return (Criteria) this;
		}

		public Criteria andConfigTypeIsNotNull() {
			addCriterion("config_type is not null");
			return (Criteria) this;
		}

		public Criteria andConfigTypeEqualTo(String value) {
			addCriterion("config_type =", value, "configType");
			return (Criteria) this;
		}

		public Criteria andConfigTypeNotEqualTo(String value) {
			addCriterion("config_type <>", value, "configType");
			return (Criteria) this;
		}

		public Criteria andConfigTypeGreaterThan(String value) {
			addCriterion("config_type >", value, "configType");
			return (Criteria) this;
		}

		public Criteria andConfigTypeGreaterThanOrEqualTo(String value) {
			addCriterion("config_type >=", value, "configType");
			return (Criteria) this;
		}

		public Criteria andConfigTypeLessThan(String value) {
			addCriterion("config_type <", value, "configType");
			return (Criteria) this;
		}

		public Criteria andConfigTypeLessThanOrEqualTo(String value) {
			addCriterion("config_type <=", value, "configType");
			return (Criteria) this;
		}

		public Criteria andConfigTypeLike(String value) {
			addCriterion("config_type like", value, "configType");
			return (Criteria) this;
		}

		public Criteria andConfigTypeNotLike(String value) {
			addCriterion("config_type not like", value, "configType");
			return (Criteria) this;
		}

		public Criteria andConfigTypeIn(List<String> values) {
			addCriterion("config_type in", values, "configType");
			return (Criteria) this;
		}

		public Criteria andConfigTypeNotIn(List<String> values) {
			addCriterion("config_type not in", values, "configType");
			return (Criteria) this;
		}

		public Criteria andConfigTypeBetween(String value1, String value2) {
			addCriterion("config_type between", value1, value2, "configType");
			return (Criteria) this;
		}

		public Criteria andConfigTypeNotBetween(String value1, String value2) {
			addCriterion("config_type not between", value1, value2, "configType");
			return (Criteria) this;
		}

		public Criteria andCreaterIsNull() {
			addCriterion("creater is null");
			return (Criteria) this;
		}

		public Criteria andCreaterIsNotNull() {
			addCriterion("creater is not null");
			return (Criteria) this;
		}

		public Criteria andCreaterEqualTo(String value) {
			addCriterion("creater =", value, "creater");
			return (Criteria) this;
		}

		public Criteria andCreaterNotEqualTo(String value) {
			addCriterion("creater <>", value, "creater");
			return (Criteria) this;
		}

		public Criteria andCreaterGreaterThan(String value) {
			addCriterion("creater >", value, "creater");
			return (Criteria) this;
		}

		public Criteria andCreaterGreaterThanOrEqualTo(String value) {
			addCriterion("creater >=", value, "creater");
			return (Criteria) this;
		}

		public Criteria andCreaterLessThan(String value) {
			addCriterion("creater <", value, "creater");
			return (Criteria) this;
		}

		public Criteria andCreaterLessThanOrEqualTo(String value) {
			addCriterion("creater <=", value, "creater");
			return (Criteria) this;
		}

		public Criteria andCreaterLike(String value) {
			addCriterion("creater like", value, "creater");
			return (Criteria) this;
		}

		public Criteria andCreaterNotLike(String value) {
			addCriterion("creater not like", value, "creater");
			return (Criteria) this;
		}

		public Criteria andCreaterIn(List<String> values) {
			addCriterion("creater in", values, "creater");
			return (Criteria) this;
		}

		public Criteria andCreaterNotIn(List<String> values) {
			addCriterion("creater not in", values, "creater");
			return (Criteria) this;
		}

		public Criteria andCreaterBetween(String value1, String value2) {
			addCriterion("creater between", value1, value2, "creater");
			return (Criteria) this;
		}

		public Criteria andCreaterNotBetween(String value1, String value2) {
			addCriterion("creater not between", value1, value2, "creater");
			return (Criteria) this;
		}

		public Criteria andCreatetimeIsNull() {
			addCriterion("createtime is null");
			return (Criteria) this;
		}

		public Criteria andCreatetimeIsNotNull() {
			addCriterion("createtime is not null");
			return (Criteria) this;
		}

		public Criteria andCreatetimeEqualTo(Date value) {
			addCriterion("createtime =", value, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeNotEqualTo(Date value) {
			addCriterion("createtime <>", value, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeGreaterThan(Date value) {
			addCriterion("createtime >", value, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeGreaterThanOrEqualTo(Date value) {
			addCriterion("createtime >=", value, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeLessThan(Date value) {
			addCriterion("createtime <", value, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeLessThanOrEqualTo(Date value) {
			addCriterion("createtime <=", value, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeIn(List<Date> values) {
			addCriterion("createtime in", values, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeNotIn(List<Date> values) {
			addCriterion("createtime not in", values, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeBetween(Date value1, Date value2) {
			addCriterion("createtime between", value1, value2, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeNotBetween(Date value1, Date value2) {
			addCriterion("createtime not between", value1, value2, "createtime");
			return (Criteria) this;
		}

		public Criteria andUpdaterIsNull() {
			addCriterion("updater is null");
			return (Criteria) this;
		}

		public Criteria andUpdaterIsNotNull() {
			addCriterion("updater is not null");
			return (Criteria) this;
		}

		public Criteria andUpdaterEqualTo(String value) {
			addCriterion("updater =", value, "updater");
			return (Criteria) this;
		}

		public Criteria andUpdaterNotEqualTo(String value) {
			addCriterion("updater <>", value, "updater");
			return (Criteria) this;
		}

		public Criteria andUpdaterGreaterThan(String value) {
			addCriterion("updater >", value, "updater");
			return (Criteria) this;
		}

		public Criteria andUpdaterGreaterThanOrEqualTo(String value) {
			addCriterion("updater >=", value, "updater");
			return (Criteria) this;
		}

		public Criteria andUpdaterLessThan(String value) {
			addCriterion("updater <", value, "updater");
			return (Criteria) this;
		}

		public Criteria andUpdaterLessThanOrEqualTo(String value) {
			addCriterion("updater <=", value, "updater");
			return (Criteria) this;
		}

		public Criteria andUpdaterLike(String value) {
			addCriterion("updater like", value, "updater");
			return (Criteria) this;
		}

		public Criteria andUpdaterNotLike(String value) {
			addCriterion("updater not like", value, "updater");
			return (Criteria) this;
		}

		public Criteria andUpdaterIn(List<String> values) {
			addCriterion("updater in", values, "updater");
			return (Criteria) this;
		}

		public Criteria andUpdaterNotIn(List<String> values) {
			addCriterion("updater not in", values, "updater");
			return (Criteria) this;
		}

		public Criteria andUpdaterBetween(String value1, String value2) {
			addCriterion("updater between", value1, value2, "updater");
			return (Criteria) this;
		}

		public Criteria andUpdaterNotBetween(String value1, String value2) {
			addCriterion("updater not between", value1, value2, "updater");
			return (Criteria) this;
		}

		public Criteria andUpdatetimeIsNull() {
			addCriterion("updatetime is null");
			return (Criteria) this;
		}

		public Criteria andUpdatetimeIsNotNull() {
			addCriterion("updatetime is not null");
			return (Criteria) this;
		}

		public Criteria andUpdatetimeEqualTo(Date value) {
			addCriterion("updatetime =", value, "updatetime");
			return (Criteria) this;
		}

		public Criteria andUpdatetimeNotEqualTo(Date value) {
			addCriterion("updatetime <>", value, "updatetime");
			return (Criteria) this;
		}

		public Criteria andUpdatetimeGreaterThan(Date value) {
			addCriterion("updatetime >", value, "updatetime");
			return (Criteria) this;
		}

		public Criteria andUpdatetimeGreaterThanOrEqualTo(Date value) {
			addCriterion("updatetime >=", value, "updatetime");
			return (Criteria) this;
		}

		public Criteria andUpdatetimeLessThan(Date value) {
			addCriterion("updatetime <", value, "updatetime");
			return (Criteria) this;
		}

		public Criteria andUpdatetimeLessThanOrEqualTo(Date value) {
			addCriterion("updatetime <=", value, "updatetime");
			return (Criteria) this;
		}

		public Criteria andUpdatetimeIn(List<Date> values) {
			addCriterion("updatetime in", values, "updatetime");
			return (Criteria) this;
		}

		public Criteria andUpdatetimeNotIn(List<Date> values) {
			addCriterion("updatetime not in", values, "updatetime");
			return (Criteria) this;
		}

		public Criteria andUpdatetimeBetween(Date value1, Date value2) {
			addCriterion("updatetime between", value1, value2, "updatetime");
			return (Criteria) this;
		}

		public Criteria andUpdatetimeNotBetween(Date value1, Date value2) {
			addCriterion("updatetime not between", value1, value2, "updatetime");
			return (Criteria) this;
		}

		public Criteria andRemarkIsNull() {
			addCriterion("remark is null");
			return (Criteria) this;
		}

		public Criteria andRemarkIsNotNull() {
			addCriterion("remark is not null");
			return (Criteria) this;
		}

		public Criteria andRemarkEqualTo(String value) {
			addCriterion("remark =", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkNotEqualTo(String value) {
			addCriterion("remark <>", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkGreaterThan(String value) {
			addCriterion("remark >", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkGreaterThanOrEqualTo(String value) {
			addCriterion("remark >=", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkLessThan(String value) {
			addCriterion("remark <", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkLessThanOrEqualTo(String value) {
			addCriterion("remark <=", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkLike(String value) {
			addCriterion("remark like", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkNotLike(String value) {
			addCriterion("remark not like", value, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkIn(List<String> values) {
			addCriterion("remark in", values, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkNotIn(List<String> values) {
			addCriterion("remark not in", values, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkBetween(String value1, String value2) {
			addCriterion("remark between", value1, value2, "remark");
			return (Criteria) this;
		}

		public Criteria andRemarkNotBetween(String value1, String value2) {
			addCriterion("remark not between", value1, value2, "remark");
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
