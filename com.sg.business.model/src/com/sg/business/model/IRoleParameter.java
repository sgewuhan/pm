package com.sg.business.model;

import java.util.Map;

public interface IRoleParameter {
	/**
	 * ���÷�Χ����ʹ�ù���
	 */
	public static final int TYPE_NONE = 0;
	
	/**
	 * ���÷�Χ����Ŀ
	 */
	public static final int TYPE_PROJECT = 1;
	
	/**
	 * ���÷�Χ������
	 */
	public static final int TYPE_WORK = 2;
	
	/**
	 * ���÷�Χ�������е�����
	 */
	public static final int TYPE_WORK_PROCESS = 3;
	
	/**
	 * ���÷�Χ
	 */
	public static final String TYPE = "type";
	

	/**
	 * ָ����Ա�б������������
	 */
	public static final String USERID = "userIdList";

	/**
	 * ��ĿID��������Ŀȡ��ɫ��Ա
	 */
	public static final String PROJECT_ID = "projectId";
	/**
	 * ��Ŀ��������Ŀȡ��ɫ��Ա
	 */
	public static final String PROJECT = "project";
	/**
	 * ��Ŀ�����ˣ�������Ŀȡ��ɫ��Ա
	 */
	public static final String PROJECT_CHARGER = "charger";
	/**
	 * ��Ŀ�����ñ�׼��������Ŀȡ��ɫ��Ա
	 */
	public static final String PROJECT_STANDARD_OPTION = "standardOption";
	/**
	 * ��Ŀ�Ĳ�Ʒ���ͣ�������Ŀȡ��ɫ��Ա
	 */
	public static final String PROJECT_PRODUCT_OPTION = "productOption";
	/**
	 * ��Ŀ����Ŀ���ͣ�������Ŀȡ��ɫ��Ա
	 */
	public static final String PROJECT_TYPE_OPTION = "projectTypeOption";
	/**
	 * ��Ŀ�����ţ�������Ŀȡ��ɫ��Ա
	 */
	public static final String PROJECT_BUSINESS_ORGANIZATION = "businessOrganization";
	/**
	 * ��Ŀ�����ţ�������Ŀȡ��ɫ��Ա
	 */
	public static final String PROJECT_FUNCTION_ORGANIZATION = "functionOrganization";
	/**
	 * ��Ŀ�е����ţ�������Ŀȡ��ɫ��Ա
	 */
	public static final String PROJECT_LAUNCH_ORGANIZATION = "launchOrganizationList";
	/**
	 * ��Ŀģ�����ƣ�������Ŀȡ��ɫ��Ա
	 */
	public static final String PROJECT_TEMPLATE_DESC = "projectTemplateDesc";


	/**
	 * ����ID�����ڹ���������ȡ��ɫ��Ա
	 */
	public static final String WORK_ID = "workId";
	/**
	 * ���������ڹ���������ȡ��ɫ��Ա
	 */
	public static final String WORK = "work";
	/**
	 * ���������ˣ����ڹ���������ȡ��ɫ��Ա
	 */
	public static final String WORK_CHARGER = "workCharger";
	/**
	 * ������̱������ڹ���������ȡ��ɫ��Ա
	 */
	public static final String WORK_MILESTONE = "milestone";
	
	/**
	 * �������ͣ����ڹ���ȡ��ɫ��Ա
	 */
	public static final String WORK_TYPE = "worktype"; 
	
	/**
	 * ���̱�������������ȡ��ɫ��Ա����ʱ�޷�ʹ��
	 */
	public static final String PROCESS_INPUT = "processinput"; //$NON-NLS-1$
	
	public Map<String, Object> getParameters();
}
