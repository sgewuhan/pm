package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;

public class CalendarSetting extends PrimaryObject {

	/**
	 * 开始日期
	 */
	public static final String F_START_DATE = "startdate";
	/**
	 * 结束日期
	 */
	public static final String F_END_DATE = "enddate";
	/**
	 * 工作日
	 */
	public static final String F_WORKINGDAY = "workingday";
	/**
	 * 条件
	 */
	public static final String F_CONDITION = "condition";
	/**
	 * 计算
	 */
	public static final String F_OPERATOR = "operator";
	/**
	 * 值
	 */
	public static final String F_VALUE = "value";
	/**
	 * 优先级
	 */
	public static final String F_SEQ = "seq";
	/**
	 * 每天工时（小时）
	 */
	public static final String F_WORKING_TIME = "worktime";

	/**
	 * 项目Id, 如果为空，该条目为系统日历
	 */
	public static final String F_PROJECT_ID = "project_id";

	/**
	 * 条件的类型，每月的日期
	 */
	public static final String CONDITON_DAY_OF_MONTH = "日";

	/**
	 * 条件的类型，每周的第几日
	 */
	public static final String CONDITON_DAY_OF_WEEK = "周";

	public static final String OPERATOR_EQ = "等于";

	public static final String OPERATOR_LT = "小于";

	public static final String OPERATOR_LE = "小于等于";

	public static final String OPERATOR_GT = "大于等于";

	public static final String OPERATOR_GE = "大于";

	private Calendar getStartDate() {
		Date date = (Date) getValue(F_START_DATE);
		return Utils.getDayBegin(date);

	}

	private Calendar getEndDate() {
		Date date = (Date) getValue(F_END_DATE);
		return Utils.getDayEnd(date);

	}

	private boolean isWorkingDay() {
		return Boolean.TRUE.equals(getValue(F_WORKINGDAY));
	}

	private Double getWorkingTime() {
		if (!isWorkingDay()) {
			return new Double(0d);
		}
		Double d = (Double) getValue(F_WORKING_TIME);
		if (d == null) {
			return new Double(0d);
		} else {
			return d;
		}
	}

	private String getOperator() {
		return (String) getValue(F_OPERATOR);
	}

	private boolean isWeekCondition() {
		return CONDITON_DAY_OF_WEEK.equals(getValue(F_CONDITION));
	}

	private boolean isDateCondition() {
		return CONDITON_DAY_OF_MONTH.equals(getValue(F_CONDITION));
	}

	private boolean hasCondition() {
		return !Utils.isNullOrEmptyString(getValue(F_CONDITION))
				&& !Utils.isNullOrEmptyString(getValue(F_OPERATOR))
				&& !Utils.isNullOrEmptyString(getValue(F_VALUE));
	}

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		System.out.println(cal.get(Calendar.DAY_OF_MONTH));
	}

	private String getConditionValue() {
		String value = (String) getValue(F_VALUE);
		return value.replaceAll("，", ",");
	}

	public Map<String, Double> getCalendarWorkingTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar start = getStartDate();
		Calendar end = getEndDate();

		HashMap<String, Double> result = new HashMap<String, Double>();
		Calendar current = start;
		while (current.before(end)) {
			String key = sdf.format(current.getTime());
			Double workingTime = getWorkingTime(current);
			if (workingTime != null) {
				result.put(key, workingTime);
			}
			current.set(Calendar.DATE, current.get(Calendar.DATE) + 1);
		}
		return result;

	}

	private Double getWorkingTime(Calendar cal) {
		if (!hasCondition()) {
			// 如果无条件，直接获取工时
			return getWorkingTime();
		}

		String value = (String) getValue(F_VALUE);

		if (isWeekCondition()) {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			if (OPERATOR_EQ.equals(getOperator())) {
				// 如果取等于，可以使用逗号分隔的值
				String[] values = getConditionValue().split(",");
				for (int i = 0; i < values.length; i++) {
					try {
						int v = Integer.parseInt(values[i]);
						v = (v == 7) ? 1 : (v + 1);

						if (dayOfWeek == v) {
							// 该日期包含在条件中
							return getWorkingTime();
						}
					} catch (Exception e) {
					}
				}
			} else {
				try {
					int v = Integer.parseInt(value);
					v = (v == 7) ? 1 : (v + 1);

					if (OPERATOR_GE.equals(getOperator())) {
						if (dayOfWeek >= v) {
							return getWorkingTime();
						}
					} else if (OPERATOR_GT.equals(getOperator())) {
						if (dayOfWeek > v) {
							return getWorkingTime();
						}
					} else if (OPERATOR_LE.equals(getOperator())) {
						if (dayOfWeek <= v) {
							return getWorkingTime();
						}
					} else if (OPERATOR_LT.equals(getOperator())) {
						if (dayOfWeek < v) {
							return getWorkingTime();
						}
					}
				} catch (Exception e) {
				}
			}
		} else if (isDateCondition()) {
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

			if (OPERATOR_EQ.equals(getOperator())) {
				// 如果取等于，可以使用逗号分隔的值
				String[] values = getConditionValue().split(",");
				for (int i = 0; i < values.length; i++) {
					try {
						int v = Integer.parseInt(values[i]);
						if (dayOfMonth == v) {
							// 该日期包含在条件中
							return getWorkingTime();
						}
					} catch (Exception e) {
					}
				}
			} else {
				try {
					int v = Integer.parseInt(value);
					if (OPERATOR_GE.equals(getOperator())) {
						if (dayOfMonth >= v) {
							return getWorkingTime();
						}
					} else if (OPERATOR_GT.equals(getOperator())) {
						if (dayOfMonth > v) {
							return getWorkingTime();
						}
					} else if (OPERATOR_LE.equals(getOperator())) {
						if (dayOfMonth <= v) {
							return getWorkingTime();
						}
					} else if (OPERATOR_LT.equals(getOperator())) {
						if (dayOfMonth < v) {
							return getWorkingTime();
						}
					}
				} catch (Exception e) {
				}
			}
		}
		return null;
	}

	public Double getCalendarWorkingTime(String yyyyMMddDate) {
		Map<String, Double> map = getCalendarWorkingTime();
		return map.get(yyyyMMddDate);
	}
}
