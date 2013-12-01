package com.sg.business.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.util.Util;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;

public abstract class ProjectProvider extends PrimaryObject {

	public ProjectSetSummaryData sum;


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

	public Object[] parameters;

	private ListenerList listeners;

	private boolean isDirty = true;

	private List<PrimaryObject> projectSetData;

	public ProjectProvider() {
		super();
		sum = new ProjectSetSummaryData();
		parameters = new Object[2];
		parameters[0] = Calendar.getInstance();
		parameters[1] = ProjectProvider.PARAMETER_SUMMARY_BY_YEAR;
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
	 * ���ò�ѯ����
	 * 
	 * @param parameters
	 */
	public void setParameters(Object[] parameters) {
		if (!Util.equals(this.parameters, parameters)) {
			Object[] oldParameters = this.parameters;
			this.parameters = new Object[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				this.parameters[i] = parameters[i];
			}
			parameterChanged(oldParameters, parameters);
			isDirty = true;
		}
	}

	/**
	 * ��ȡ��ѯ����
	 * 
	 * @return
	 */
	public Object[] getParameters() {
		return this.parameters;
	}

	final protected Date getStartDate() throws Exception {
		Date start;
		Calendar calendar = (Calendar) parameters[0];
		switch ((String) parameters[1]) {
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

	final protected Date getEndDate() throws Exception {
		Date end;
		Calendar calendar = Calendar.getInstance();
		Date start = getStartDate();
		calendar.setTime(start);
		switch ((String) parameters[1]) {
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

	public void addParameterChangedListener(IParameterListener listener) {
		if (listeners == null) {
			listeners = new ListenerList();
		}
		listeners.add(listener);
	}

	private void parameterChanged(Object[] oldParameters,
			Object[] newParameters) {
		if (listeners != null && listeners.size() > 0) {
			Object[] lis = listeners.getListeners();
			for (int i = 0; i < lis.length; i++) {
				((IParameterListener) lis[i]).parameterChanged(oldParameters,
						newParameters);
			}
		}
	}

	public List<PrimaryObject> getData() {
		if (isDirty) {
			projectSetData = getProjectSet();
			isDirty = false;
		}
		return projectSetData;
	}


}
