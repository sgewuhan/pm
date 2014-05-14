package com.sg.business.model;

/**
 * ģ�ͳ���</p>
 * ������ģ�ʹ洢�����ݿ�ʹ洢�ı�
 * @author jinxitao
 *
 */
public interface IModelConstants {

	/**
	 * ע�����ݿ�
	 */
	public final static String DB = "pm2"; //$NON-NLS-1$

	/**
	 * ��֯����
	 */
	public final static String C_ORGANIZATION = "organization"; //$NON-NLS-1$

	/**
	 * ��ɫ����
	 */
	public static final String C_ROLE = "role"; //$NON-NLS-1$

	/**
	 * �ʻ�����
	 */
	public static final String C_USER = "account"; //$NON-NLS-1$

	/**
	 * ��ɫָ�ɼ���
	 */
	public static final String C_ROLE_ASSIGNMENT = "roleassignment"; //$NON-NLS-1$

	/**
	 * Ŀ¼����
	 */
	public static final String C_FOLDER = "folder"; //$NON-NLS-1$

	/**
	 * �ĵ�����
	 */
	public static final String C_DOCUMENT = "document"; //$NON-NLS-1$

	/**
	 * ��������
	 */
	public static final String C_WORK_DEFINITION = "workd"; //$NON-NLS-1$

	/**
	 * ��Ŀģ��
	 */
	public static final String C_PROJECT_TEMPLATE = "projecttemplate"; //$NON-NLS-1$
	
	/**
	 * ��ʱ����
	 */
	public static final String C_WORKTIMEPROGRAM="worktimeprogram";

	/**
	 * Ԥ���Ŀ
	 */
	public static final String C_BUDGET_ITEM = "budget"; //$NON-NLS-1$
	
	/**
	 * ϵͳ����
	 */
	public static final String C_CALENDAR_SETTING = "calendarsetting"; //$NON-NLS-1$
	
	/**
	 * ��ɫ����
	 */
	public static final String C_ROLE_DEFINITION = "roled"; //$NON-NLS-1$


	/**
	 * �ĵ�ģ��
	 */
	public static final String C_DOCUMENT_DEFINITION = "documentd"; //$NON-NLS-1$

	/**
	 * �����ﶨ��
	 */
	public static final String C_DELIEVERABLE_DEFINITION = "deliverabled"; //$NON-NLS-1$

	/**
	 * ��������ǰ���ù�ϵ
	 */
	public static final String C_WORK_DEFINITION_CONNECTION = "workdconnection"; //$NON-NLS-1$

	/**
	 * ��Ŀ����
	 */
	public static final String C_PROJECT = "project"; //$NON-NLS-1$

	/**
	 * �����Զ����
	 */
	public static final String C__IDS = "ids"; //$NON-NLS-1$

	/**
	 * 
	 * ��ʱ��Ч����
	 */
	public static final String C_WORKS_PERFORMENCE = "worksperformence"; //$NON-NLS-1$

	/**
	 * ��ʱ��������
	 */
	public static final String C_WORKS_ALLOCATE = "worksallocate"; //$NON-NLS-1$

	/**
	 * �ɱ���Ŀ���ձ�
	 */
	public static final String C_COSTACCOUNT_ITEM = "costaccount"; //$NON-NLS-1$

	/**
	 * �ɱ����ĵ��ڼ�ɱ�����SAP��ȡ��
	 */
	public static final String C_RND_PEROIDCOST_COSTCENTER = "rndcost"; //$NON-NLS-1$
	

	/**
	 * ������ŵ��ڼ�ɱ�����̯����󱣴棩
	 */
	public static final String C_RND_PEROIDCOST_ALLOCATION = "rndcostallocation"; //$NON-NLS-1$

	/**
	 * ������ŵĶ�Ӧ�з��ɱ�
	 */
	public static final String C_WORKORDER_COST = "workordercost"; //$NON-NLS-1$
	/**
	 * ��֯id��Ӧ�Ĺ������
	 */
	public static final String C_COMPANY_WORKORDER = "companyworkorders"; //$NON-NLS-1$
	
	/**
	 * ��Ŀ��ŵ��Զ������������,��Project.doInsert()�еı���ʹ��
	 */
	public static final String SEQ_PROJECT_NUMBER = "projectnumber"; //$NON-NLS-1$
	
	/**
	 * �ĵ�ģ���Զ����
	 */
	public static final String SEQ_DOCUMENT_TEMPLATE_NUMBER = "documenttemplatenumber"; //$NON-NLS-1$
	
	
	/**
	 * �ĵ��Զ����
	 */
	public static final String SEQ_DOCUMENT_NUMBER = "documentnumber"; //$NON-NLS-1$

	/**
	 * ��Ŀ��ɫ
	 */
	public static final String C_PROJECT_ROLE = "projectrole"; //$NON-NLS-1$

	/**
	 * ��������
	 */
	public static final String C_WORK = "work"; //$NON-NLS-1$

	/**
	 * �����Ｏ��
	 */
	public static final String C_DELIEVERABLE = "deliverable"; //$NON-NLS-1$

	/**
	 * ǰ���ù�ϵ
	 */
	public static final String C_WORK_CONNECTION = "workconnection"; //$NON-NLS-1$

	/**
	 * ��ĿԤ��
	 */
	public static final String C_PROJECT_BUDGET = "projectbudget"; //$NON-NLS-1$

	
	/**
	 * ��ɫָ��
	 */
	public static final String C_PROJECT_ROLE_ASSIGNMENT = "projectroleassignment"; //$NON-NLS-1$

	/**
	 * ��Ϣ
	 */
	public static final String C_MESSAGE = "message"; //$NON-NLS-1$

	/**
	 * �����
	 */
	public static final String C_BULLETINBOARD = "bulletinboard"; //$NON-NLS-1$
	

	public static final String C_SETTING = "setting"; //$NON-NLS-1$

	/**
	 * ����: Ĭ�ϵ���Ŀ�ύ����������,������ֻ������ϵͳ����Interval
	 */
	public static final String S_DEFAULT_PROJECT_COMMIT_DURATION = "PROJECT.COMMIT.DURATION"; //$NON-NLS-1$
	
	/**
	 * ���ã�����������ˢ�����ڣ������ÿ������û����ú�ϵͳ����
	 */
	public static final String S_U_WORK_RESERVED_REFRESH_INTERVAL = "WORK.RESERVED.REFRESH.INTERVAL"; //$NON-NLS-1$

	
	/**
	 * ���ã�����������ˢ�����ڣ������ÿ�����ϵͳ����
	 */
	public static final String S_S_WORK_RESERVED_REFRESH_INTERVAL = "WORK.RESERVED.REFRESH.SYSTEM"; //$NON-NLS-1$

	/**
	 * ����: BI���ݻ������ʱ���� Сʱ
	 */
	public static final String S_S_BI_DATARELOAD_INTERVAL = "BI.CACHE.DATA_RELOAD.INTERVAL"; //$NON-NLS-1$
	
	/**
	 * ���ã��ռ����ˢ�¼��ʱ��
	 */
	public static final String S_U_MESSAGE_RESERVED_REFRESH_INTERVAL = "MESSAGEINBOX.REFRESH.INTERVAL"; //$NON-NLS-1$
	
	
	/**
	 * ���ã��ռ����ˢ�¼��ʱ��
	 */
	public static final String S_U_TASK_RESERVED_REFRESH_INTERVAL = "TASKRESERVED.REFRESH.INTERVAL"; //$NON-NLS-1$

	
	/**
	 * ���ã����̿��URL��ַ
	 * 
	 */
	public static final String S_PROCESS_BASE_URL = "SYSTEM.PROCESS_BASE_URL"; //$NON-NLS-1$

	/**
	 * ���ã�SAP�ͻ������������
	 */
	public static final String S_EAI_SAP_MAXCONN = "EAI.SAP.MAXCONN"; //$NON-NLS-1$


	/**
	 * ���ã�SAP�ͻ�������
	 */
	public static final String S_EAI_SAP_CLIENT = "EAI.SAP.CLIENT"; //$NON-NLS-1$

	/**
	 * ���ã�SAP�ͻ����û�ID
	 */
	public static final String S_EAI_SAP_USERID = "EAI.SAP.USERID"; //$NON-NLS-1$

	/**
	 * ���ã�SAP�û���¼����
	 */
	public static final String S_EAI_SAP_PASSWORD = "EAI.SAP.PASSWORD"; //$NON-NLS-1$

	/**
	 * ���ã�SAP��������
	 */
	public static final String S_EAI_SAP_LANGUAGE = "EAI.SAP.LANG"; //$NON-NLS-1$

	/**
	 * ���ã�SAP����IP
	 */
	public static final String S_EAI_SAP_HOST = "EAI.SAP.HOST"; //$NON-NLS-1$

	/**
	 * ���ã�SAPʵ�����
	 */
	public static final String S_EAI_SAP_INSTANCENUMBER = "EAI.SAP.INSTANCENUMBER"; //$NON-NLS-1$

	/**
	 * ���ã�Ĭ�Ϲ�������Ԥ��ʱ��
	 */
	public static final String S_S_WORK_REMIND_BEFORE = "WORK.REMIND.BEFORE";
	
	/**
	 * ���ã��û�����������
	 */
	public static final String C_USERTASK = "usertask"; //$NON-NLS-1$

	/**
	 * ���ã����������ӳٱ��ʱ�䣨���ӣ�
	 */
	public static final String S_S_TASK_DELAY = "TASK.DELAYMARK"; //$NON-NLS-1$
	
	/**
	 * ���ã��ɱ�����Ԥ������
	 */
	public static final String S_S_BI_OVER_COST_ESTIMATE = "BI.OVER.COST.ESTIMATE"; //$NON-NLS-1$

	/**
	 * ���ã����ŷָ����Ҫ�汾������
	 */
	public static final String S_MAJOR_VID_SEQ = "MAJOR_VID_SEQ"; //$NON-NLS-1$

	/**
	 * ��Ʒ���ʱ���
	 */
	public static final String C_PRODUCT = "productitem"; //$NON-NLS-1$

	/**
	 * �û���Ŀ��
	 */
	public static final String C_USERPROJECTPERF = "userprojectperf"; //$NON-NLS-1$

	/**
	 * ��������
	 */
	public static final String C_SALESDATA = "salesdata"; //$NON-NLS-1$

	public static final String SEQ_BUG = "bugid"; //$NON-NLS-1$

	public static final String C_BUG = "buginfo"; //$NON-NLS-1$

	public static final String C_PROJECT_MONTH_DATA = "projectmonthdata"; //$NON-NLS-1$

	public static final String C_PRODUCT_SALESDATA = "productsalesdata";

	public static final String C_USER_PREFORMENCE = "userpref";

	

}
