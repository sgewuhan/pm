package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;

public class WorkTimeProgram extends PrimaryObject {
	/**
	 * ������
	 */
	public static final String F_COLUMNTYPES="columntypes";
	/**
	 * ��ʱ����
	 */
	public static final String F_WORKTIMETYPES="worktimetypes";
	/**
	 * ��ʱ����
	 */
	public static final String F_WORKTIMEDATA="worktimedata";
	/**
	 * ˵��
	 */
	public static final String F_DESCRIPTION="description";
	/**
	 * �Ƿ�����
	 */
	public static final String F_ACTIVATED="activated";
	
	/**
	 * ������֯ID
	 * 
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_ID = "organization_id"; //$NON-NLS-1$
	
	
	/**
	 * ����ѡ�����ColumnType �Ӽ�¼���ֶΣ�DBObject����
	 */
	public static final String F_TYPE_OPTIONS = "options";
	


}
