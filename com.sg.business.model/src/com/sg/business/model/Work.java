package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;


public class Work extends PrimaryObject implements IWorkCloneFields{

	/**
	 * ������Ŀid, �ֶΣ�������Ŀ_id��ֵ
	 */
	public static final String F_PROJECT_ID = "project_id";

	/**
	 * ������_id�ֶΣ����ڱ����������_id��ֵ
	 */
	public static final String F_ROOT_ID = "root_id";

	/**
	 * ����ģ�����ɾ�����������͵��ֶ�
	 */
	public static final String F_MONDARY = "mondary";

	public static final String F_PARENT_ID = "parent_id";

	
}
