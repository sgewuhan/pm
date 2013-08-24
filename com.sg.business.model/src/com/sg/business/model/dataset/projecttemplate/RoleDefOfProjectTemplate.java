package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.RoleDefinition;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ��ɫ����
 * <p/> 
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ�ȡҵ���������ѡ��Ŀģ��Ľ�ɫ������Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ��ɫ����������Ϣ
 * <li>ͨ��F_PROJECT_TEMPLATE_ID������ѡ��Ŀģ��
 * 
 * @author yangjun
 *
 */
public class RoleDefOfProjectTemplate extends MasterDetailDataSetFactory {

	/**
	 * ��ȡ��ɫ����������Ϣ,�������ý�ɫ���ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public RoleDefOfProjectTemplate() {
		//���ý�ɫ���ϵĴ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ROLE_DEFINITION);
	}

	/**
	 * ���ý�ɫ��������Ŀģ��Ĺ����ֶΣ�{@link com.sg.business.model.RoleDefinition.F_PROJECT_TEMPLATE_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return RoleDefinition.F_PROJECT_TEMPLATE_ID;
	}

}
