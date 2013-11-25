package com.sg.business.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;

public abstract class ProjectProvider extends PrimaryObject {

	
	public ProjectSetSummaryData summaryData;
	
	
	/**
	 * �ɱ�����
	 */
	public static String F_SUMMARY_NORMAL_COST = "s11";

	/**
	 * �ɱ���֧
	 */
	public static String F_SUMMARY_OVER_COST = "s12";
	
	

	/**
	 * �������ƣ��������
	 */
	public static final String PARAMETER_SUMMARY_BY_YEAR = "y";

	/**
	 * ��������:�����ȼ���
	 */
	public static final String PARAMETER_SUMMARY_BY_QUARTER = "q";

	/**
	 * ����:���¼���
	 */
	public static final String PARAMETER_SUMMARY_BY_MONTH = "m";

	private Object[] parametes;

	private HashMap<String, Object> summaryInfor;

	public ProjectProvider() {
		super();
		summaryInfor = new HashMap<String, Object>();
	}

	public abstract List<PrimaryObject> getProjectSet();

	@Override
	public boolean doSave(IContext context) throws Exception {
		return true;
	}

	@Override
	public void doUpdate(IContext context) throws Exception {
	}

	@Override
	public void doInsert(IContext context) throws Exception {
	}

	/**
	 * �����Ŀ���ϵ�����
	 * 
	 * @return
	 */
	public abstract String getProjectSetName();

	/**
	 * �����Ŀ������ͼƬ
	 * 
	 * @return
	 */
	public abstract String getProjectSetCoverImage();

	/**
	 * �����Ŀ�� ժҪ����
	 * 
	 * @param key
	 *            ժҪ���ݵ��ֶ���
	 * @param year
	 * @return
	 */
	public final Object getSummaryValue(String key) {
		return summaryInfor.get(key);
	}

	/**
	 * ���úϼ�ֵ
	 * 
	 * @param data
	 */
	public final void setSummaryDate(Map<String, Object> data) {
		summaryInfor.clear();
		summaryInfor.putAll(data);
	}

	/**
	 * ���ò�ѯ����
	 * 
	 * @param parameters
	 */
	public void setParameters(Object[] parameters) {
		this.parametes = parameters;
	}

	/**
	 * ��ȡ��ѯ����
	 * 
	 * @return
	 */
	public Object[] getParameters() {
		return this.parametes;
	}

	public Date getStartDate() throws Exception {
		// TODO �������������ʼʱ��
		// parametes[0] Ϊ����ʱ��
		// parameters[1]Ϊ�������ο�PARA��ͷ�ĳ�����
		// ���parameters[1] ΪPARAMETER_SUMMARY_BY_YEAR,
		// Ӧ�ý�parameters[0]ǿתΪCalender�����ظ���ĵ�һ��
		// ��������ջ�����׳��쳣
		Date start;
		Calendar calendar = (Calendar) parametes[0];
		switch ((String) parametes[1]) {
		case PARAMETER_SUMMARY_BY_YEAR:
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			start = calendar.getTime();
			return start;
		case PARAMETER_SUMMARY_BY_QUARTER:
			int i = calendar.get(Calendar.MONTH);
			calendar.set(Calendar.MONTH, i / 3 * 3);
			calendar.set(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			start = calendar.getTime();
			return start;
		case PARAMETER_SUMMARY_BY_MONTH:
			calendar.set(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			start = calendar.getTime();
			return start;
		default:
			throw new Exception("ʱ������쳣");
		}
	}

	public Date getEndDate() throws Exception {
		Date end;
		Calendar calendar = Calendar.getInstance();
		Date start = getStartDate();
		calendar.setTime(start);
		switch ((String) parametes[1]) {
		case PARAMETER_SUMMARY_BY_YEAR:
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
			calendar.set(Calendar.MILLISECOND,
					calendar.get(Calendar.MILLISECOND) - 1);
			end = calendar.getTime();
			return end;
		case PARAMETER_SUMMARY_BY_QUARTER:
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 3);
			calendar.set(Calendar.MILLISECOND,
					calendar.get(Calendar.MILLISECOND) - 1);
			end = calendar.getTime();
			return end;
		case PARAMETER_SUMMARY_BY_MONTH:
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
			calendar.set(Calendar.MILLISECOND,
					calendar.get(Calendar.MILLISECOND) - 1);
			end = calendar.getTime();
			return end;
		default:
			throw new Exception("ʱ������쳣");
		}
	}

//	public abstract void setF_SUMMARY_TOTAL(int count);
//	public abstract void setF_SUMMARY_FINISHED(int count);
//	public abstract void setF_SUMMARY_PROCESSING(int count);
//	
//	public abstract void setF_SUMMARY_NORMAL_PROCESS(int count);
//
//	public abstract void setF_SUMMARY_DELAY(int count);
//
//	public abstract void setF_SUMMARY_ADVANCE(int count);
//
//	public abstract void setF_SUMMARY_NORMAL_COST(int count);
//
//	public abstract void setF_SUMMARY_OVER_COST(int count);
	
	

}
