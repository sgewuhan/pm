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
	public final static String DB = "pm2";

	/**
	 * ��֯����
	 */
	public final static String C_ORGANIZATION = "organization";

	/**
	 * ��ɫ����
	 */
	public static final String C_ROLE = "role";

	/**
	 * �ʻ�����
	 */
	public static final String C_USER = "account";

	/**
	 * ��ɫָ�ɼ���
	 */
	public static final String C_ROLE_ASSIGNMENT = "roleassignment";

	/**
	 * Ŀ¼����
	 */
	public static final String C_FOLDER = "folder";

	/**
	 * �ĵ�����
	 */
	public static final String C_DOCUMENT = "document";

	/**
	 * ��������
	 */
	public static final String C_WORK_DEFINITION = "workd";

	/**
	 * ��Ŀģ��
	 */
	public static final String C_PROJECT_TEMPLATE = "projecttemplate";

	/**
	 * Ԥ���Ŀ
	 */
	public static final String C_BUDGET_ITEM = "budget";
	
	/**
	 * ϵͳ����
	 */
	public static final String C_CALENDAR_SETTING = "calendarsetting";
	
	/**
	 * ��ɫ����
	 */
	public static final String C_ROLE_DEFINITION = "roled";


	/**
	 * �ĵ�ģ��
	 */
	public static final String C_DOCUMENT_DEFINITION = "documentd";

	/**
	 * �����ﶨ��
	 */
	public static final String C_DELIEVERABLE_DEFINITION = "deliverabled";

	/**
	 * ��������ǰ���ù�ϵ
	 */
	public static final String C_WORK_DEFINITION_CONNECTION = "workdconnection";

	/**
	 * ��Ŀ����
	 */
	public static final String C_PROJECT = "project";

	/**
	 * �����Զ����
	 */
	public static final String C__IDS = "ids";

	/**
	 * 
	 * ��ʱ��Ч����
	 */
	public static final String C_WORKS_PERFORMENCE = "worksperformence";

	/**
	 * ��ʱ��������
	 */
	public static final String C_WORKS_ALLOCATE = "worksallocate";

	/**
	 * �ɱ���Ŀ���ձ�
	 */
	public static final String C_COSTACCOUNT_ITEM = "costaccount";

	/**
	 * �ɱ����ĵ��ڼ�ɱ�����SAP��ȡ��
	 */
	public static final String C_RND_PEROIDCOST_COSTCENTER = "rndcost";
	

	/**
	 * ������ŵ��ڼ�ɱ�����̯����󱣴棩
	 */
	public static final String C_RND_PEROIDCOST_ALLOCATION = "rndcostallocation";

	/**
	 * ��֯id��Ӧ�Ĺ������
	 */
	public static final String C_COMPANY_WORKORDER = "companyworkorders";
	
	/**
	 * ��Ŀ��ŵ��Զ������������,��Project.doInsert()�еı���ʹ��
	 */
	public static final String SEQ_PROJECT_NUMBER = "projectnumber";
	
	/**
	 * �ĵ�ģ���Զ����
	 */
	public static final String SEQ_DOCUMENT_TEMPLATE_NUMBER = "documenttemplatenumber";

	/**
	 * ��Ŀ��ɫ
	 */
	public static final String C_PROJECT_ROLE = "projectrole";

	/**
	 * ��������
	 */
	public static final String C_WORK = "work";

	/**
	 * �����Ｏ��
	 */
	public static final String C_DELIEVERABLE = "deliverable";

	/**
	 * ǰ���ù�ϵ
	 */
	public static final String C_WORK_CONNECTION = "workconnection";

	/**
	 * ��ĿԤ��
	 */
	public static final String C_PROJECT_BUDGET = "projectbudget";

	
	/**
	 * ��ɫָ��
	 */
	public static final String C_PROJECT_ROLE_ASSIGNMENT = "projectroleassignment";

	/**
	 * ��Ϣ
	 */
	public static final String C_MESSAGE = "message";

	/**
	 * �����
	 */
	public static final String C_BULLETINBOARD = "bulletinboard";
	

	public static final String C_SETTING = "setting";

	/**
	 * ����: Ĭ�ϵ���Ŀ�ύ����������,������ֻ������ϵͳ����Interval
	 */
	public static final String S_DEFAULT_PROJECT_COMMIT_DURATION = "PROJECT.COMMIT.DURATION";
	
	/**
	 * ���ã�����������ˢ�����ڣ������ÿ������û����ú�ϵͳ����
	 */
	public static final String S_U_WORK_RESERVED_REFRESH_INTERVAL = "WORK.RESERVED.REFRESH.INTERVAL";

	
	/**
	 * ���ã�����������ˢ�����ڣ������ÿ�����ϵͳ����
	 */
	public static final String S_S_WORK_RESERVED_REFRESH_INTERVAL = "WORK.RESERVED.REFRESH.SYSTEM";

	/**
	 * ���ã��ռ����ˢ�¼��ʱ��
	 */
	public static final String S_U_MESSAGE_RESERVED_REFRESH_INTERVAL = "MESSAGEINBOX.REFRESH.INTERVAL";

	
	/**
	 * ���̿��URL��ַ
	 * 
	 */
	public static final String S_PROCESS_BASE_URL = "SYSTEM.PROCESS_BASE_URL";

	/**
	 * SAP�ͻ������������
	 */
	public static final String S_EAI_SAP_MAXCONN = "EAI.SAP.MAXCONN";


	/**
	 * SAP�ͻ�������
	 */
	public static final String S_EAI_SAP_CLIENT = "EAI.SAP.CLIENT";

	/**
	 * SAP�ͻ����û�ID
	 */
	public static final String S_EAI_SAP_USERID = "EAI.SAP.USERID";

	/**
	 * SAP�û���¼����
	 */
	public static final String S_EAI_SAP_PASSWORD = "EAI.SAP.PASSWORD";

	/**
	 * SAP��������
	 */
	public static final String S_EAI_SAP_LANGUAGE = "EAI.SAP.LANG";

	/**
	 * SAP����IP
	 */
	public static final String S_EAI_SAP_HOST = "EAI.SAP.HOST";

	/**
	 * SAPʵ�����
	 */
	public static final String S_EAI_SAP_INSTANCENUMBER = "EAI.SAP.INSTANCENUMBER";

	/**
	 * �û�����������
	 */
	public static final String C_USERTASK = "usertask";



}
