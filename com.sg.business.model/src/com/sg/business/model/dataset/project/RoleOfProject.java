package com.sg.business.model.dataset.project;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProjectRole;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
/**
 * <p>
 * ��Ŀ��ɫ����
 * </p>
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ����Ŀ��ɫ�ļ�����Ϣ��Ӧ�����½���Ŀ����Ŀ���ɫ��ʾ<br/>
 * �������¹��ܣ�
 * <li>��ȡ��Ŀ��ɫ����������Ϣ
 * <li>ͨ��PROJECTID������ѡ��Ŀ
 * 
 * @author yangjun
 *
 */
public class RoleOfProject extends MasterDetailDataSetFactory {

	/**
	 * ��Ŀ��ɫ�ļ��Ϲ��캯��,����������Ŀ��ɫ�ļ��ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public RoleOfProject() {
		//������Ŀ��ɫ�ļ��ϵĴ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_PROJECT_ROLE);
	}

	/**
	 * ������Ŀ��ɫ��������Ŀ�Ĺ����ֶΣ�{@link com.sg.business.model.IProjectRelative.F_PROJECT_ID }
	 */
	@Override
	protected String getDetailCollectionKey() {
		return ProjectRole.F_PROJECT_ID;
	}

}
