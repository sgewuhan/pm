package com.sg.business.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;

public abstract class ProjectProvider extends PrimaryObject {

	/**
	 * ��Ŀ����
	 */
	public static final String F_SUMMARY_TOTAL = "s1";
	
	/**
	 * �����Ŀ��
	 */
	public static final String F_SUMMARY_FINISHED = "s2";

	/**
	 * ��չ����Ŀ��
	 */
	public static final String F_SUMMARY_PROCESSING = "s3";

	/**
	 * ����������
	 */
	public static final String F_SUMMARY_NORMAL_PROCESS = "s4";

	/**
	 * ����
	 */
	public static final String F_SUMMARY_DELAY = "s5";

	/**
	 * ��ǰ
	 */
	public static final String F_SUMMARY_ADVANCE = "s8";

	/**
	 * �ɱ�����
	 */
	public static final String F_SUMMARY_NORMAL_COST = "s6";
	
	/**
	 * �ɱ���֧
	 */
	public static final String F_SUMMARY_OVER_COST = "s7";
	

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
	
	
	public ProjectProvider(){
		super();
		summaryInfor = new HashMap<String,Object>();
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
	 * @return
	 */
	public abstract String getProjectSetName() ;

	/**
	 * �����Ŀ������ͼƬ
	 * @return
	 */
	public abstract String getProjectSetCoverImage();

	/**
	 * �����Ŀ�� ժҪ����
	 * @param key ժҪ���ݵ��ֶ���
	 * @param year 
	 * @return
	 */
	public final Object getSummaryValue(String key){
		return summaryInfor.get(key);
	}
	
	/**
	 * ���úϼ�ֵ
	 * @param data
	 */
	public final void setSummaryDate(Map<String,Object>data){
		summaryInfor.clear();
		summaryInfor.putAll(data);
	}

	/**
	 * ���ò�ѯ����
	 * @param parameters
	 */
	public void setParameters(Object[] parameters) {
		this.parametes = parameters;
	}
	
	/**
	 * ��ȡ��ѯ����
	 * @return
	 */
	public Object[] getParameters(){
		return this.parametes;
	}
	
	public Date getStartDate(){
		//TODO �������������ʼʱ��
		//parametes[0] Ϊ����ʱ��
		//parameters[1]Ϊ�������ο�PARA��ͷ�ĳ�����
		//���parameters[1] ΪPARAMETER_SUMMARY_BY_YEAR, 
		//Ӧ�ý�parameters[0]ǿתΪCalender�����ظ���ĵ�һ��
		//��������ջ�����׳��쳣
		return null;
	}
	
	public Date getEndDate(){
		//TODO ����������ý���ʱ��
		//�ο���ÿ�ʼʱ��ķ���
		return null;
	}

}
