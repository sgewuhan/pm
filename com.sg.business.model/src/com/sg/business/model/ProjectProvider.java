package com.sg.business.model;

import java.util.List;

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
	public static final String F_SUMMARY_ADVANCE = null;

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
	public abstract Object getSummaryValue(String key, Object...objects);
	
	

}
