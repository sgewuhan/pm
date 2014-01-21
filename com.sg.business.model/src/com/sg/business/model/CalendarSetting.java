package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.nls.Messages;

/**
 * ��������
 * @author jinxitao
 *
 */
public class CalendarSetting extends PrimaryObject {

	/**
	 * ��ʼ����
	 */
	public static final String F_START_DATE = "startdate"; //$NON-NLS-1$
	/**
	 * ��������
	 */
	public static final String F_END_DATE = "enddate"; //$NON-NLS-1$
	/**
	 * ������
	 */
	public static final String F_WORKINGDAY = "workingday"; //$NON-NLS-1$
	/**
	 * ����
	 */
	public static final String F_CONDITION = "condition"; //$NON-NLS-1$
	/**
	 * ������
	 */
	public static final String F_OPERATOR = "operator"; //$NON-NLS-1$
	/**
	 * ֵ
	 */
	public static final String F_VALUE = "value"; //$NON-NLS-1$
	/**
	 * ���ȼ�
	 */
	public static final String F_SEQ = "seq"; //$NON-NLS-1$
	/**
	 * ÿ�칤ʱ��Сʱ��
	 */
	public static final String F_WORKING_TIME = "worktime"; //$NON-NLS-1$

	/**
	 * ��ĿId, ���Ϊ�գ�����ĿΪϵͳ����
	 */
	public static final String F_PROJECT_ID = "project_id"; //$NON-NLS-1$

	/**
	 * ���������ͣ�ÿ�µ�����
	 */
	public static final String CONDITON_DAY_OF_MONTH = Messages.get().CalendarSetting_6;

	/**
	 * ���������ͣ�ÿ�ܵĵڼ���
	 */
	public static final String CONDITON_DAY_OF_WEEK = Messages.get().CalendarSetting_5;

	public static final String OPERATOR_EQ = Messages.get().CalendarSetting_4;

	public static final String OPERATOR_LT = Messages.get().CalendarSetting_3;

	public static final String OPERATOR_LE = Messages.get().CalendarSetting_2;

	public static final String OPERATOR_GT = Messages.get().CalendarSetting_1;

	public static final String OPERATOR_GE = Messages.get().CalendarSetting_0;
	
	private HashMap<String, Double> workingTimeMap;

	/**
	 * ���ؿ�ʼ����
	 * @return Calendar
	 */
	private Calendar getStartDate() {
		Date date = (Date) getValue(F_START_DATE);
		return Utils.getDayBegin(date);

	}

	/**
	 * ���ؽ�������
	 * @return Calendar
	 */
	private Calendar getEndDate() {
		Date date = (Date) getValue(F_END_DATE);
		return Utils.getDayEnd(date);

	}

	/**
	 * �ж��Ƿ�Ϊ������
	 * @return boolean
	 */
	private boolean isWorkingDay() {
		return Boolean.TRUE.equals(getValue(F_WORKINGDAY));
	}

	/**
	 * ���ظù����յĹ���ʱ��
	 * @return
	 */
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

	/**
	 * ����
	 * @return String
	 */
	private String getOperator() {
		return (String) getValue(F_OPERATOR);
	}

	/**
	 * �ж��Ƿ�Ϊ������
	 * @return
	 */
	private boolean isWeekCondition() {
		return CONDITON_DAY_OF_WEEK.equals(getValue(F_CONDITION));
	}

	/**
	 * �ж��Ƿ�Ϊ����������
	 * @return
	 */
	private boolean isDateCondition() {
		return CONDITON_DAY_OF_MONTH.equals(getValue(F_CONDITION));
	}

	/**
	 * �ж��Ƿ���������������
	 * @return boolean
	 */
	private boolean hasCondition() {
		return !Utils.isNullOrEmptyString(getValue(F_CONDITION))
				&& !Utils.isNullOrEmptyString(getValue(F_OPERATOR))
				&& !Utils.isNullOrEmptyString(getValue(F_VALUE));
	}


	/**
	 * �����������ü�¼ֵ
	 * @return String
	 */
	private String getConditionValue() {
		String value = (String) getValue(F_VALUE);
		return value.replaceAll("��", ","); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * ����
	 */
	@Override
	public boolean doSave(IContext context) throws Exception {
		workingTimeMap = null;
		return super.doSave(context);
	}
	
	/**
	 * ������������ʱ��
	 * @return
	 */
	public Map<String, Double> getCalendarWorkingTime() {
		if(workingTimeMap == null){
			initTimeMap();
		}
		return workingTimeMap;
	}

	private void initTimeMap() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); //$NON-NLS-1$
		Calendar start = getStartDate();
		Calendar end = getEndDate();

		workingTimeMap = new HashMap<String, Double>();
		Calendar current = start;
		while (current.before(end)) {
			String key = sdf.format(current.getTime());
			Double workingTime = getWorkingTime(current);
			if (workingTime != null) {
				workingTimeMap.put(key, workingTime);
			}
			current.set(Calendar.DATE, current.get(Calendar.DATE) + 1);
		}
	}

	private Double getWorkingTime(Calendar cal) {
		if (!hasCondition()) {
			// �����������ֱ�ӻ�ȡ��ʱ
			return getWorkingTime();
		}

		String value = (String) getValue(F_VALUE);

		if (isWeekCondition()) {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			if (OPERATOR_EQ.equals(getOperator())) {
				// ���ȡ���ڣ�����ʹ�ö��ŷָ���ֵ
				String[] values = getConditionValue().split(","); //$NON-NLS-1$
				for (int i = 0; i < values.length; i++) {
					try {
						int v = Integer.parseInt(values[i]);
						v = (v == 7) ? 1 : (v + 1);

						if (dayOfWeek == v) {
							// �����ڰ�����������
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
				// ���ȡ���ڣ�����ʹ�ö��ŷָ���ֵ
				String[] values = getConditionValue().split(","); //$NON-NLS-1$
				for (int i = 0; i < values.length; i++) {
					try {
						int v = Integer.parseInt(values[i]);
						if (dayOfMonth == v) {
							// �����ڰ�����������
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

	/**
	 * ����ʽ���ع�����
	 * @param yyyyMMddDate
	 * @return
	 */
	public Double getCalendarWorkingTime(String yyyyMMddDate) {
		Map<String, Double> map = getCalendarWorkingTime();
		return map.get(yyyyMMddDate);
	}
	
	@Override
	public boolean canEdit(IContext context) {
		// TODO Auto-generated method stub
		return super.canEdit(context);
	}
	
	@Override
	public boolean canRead(IContext context) {
		// TODO Auto-generated method stub
		return super.canRead(context);
	}
	
	@Override
	public boolean canDelete(IContext context) {
		// TODO Auto-generated method stub
		return super.canDelete(context);
	}
	
	
}
