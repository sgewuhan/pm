package com.sg.business.model;

/**
 * ��Ŀ����Ŀ���ݵĹ���
 * @author jinxitao
 *
 */
public interface IProjectRelative {
	
	/**
	 * ������Ŀ_id�ֶΣ����Ա�����Ŀ_id��ֵ
	 */
	public static final String F_PROJECT_ID = "project_id"; //$NON-NLS-1$

	/**
	 * ����������Ŀ
	 * @return Project
	 */
	public Project getProject();

}
