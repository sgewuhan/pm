package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;


public class Work extends PrimaryObject implements IWorkCloneFields,IProjectRelative{

	/**
	 * ������_id�ֶΣ����ڱ����������_id��ֵ
	 */
	public static final String F_ROOT_ID = "root_id";

	/**
	 * ����ģ�����ɾ�����������͵��ֶ�
	 */
	public static final String F_MANDATORY = "mandatory";

	public static final String F_PARENT_ID = "parent_id";

	
}
