package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ��Ŀģ�漯��
 * <p/> 
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ�ȡҵ���������ѡ��֯����Ŀģ�漯����Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡͨ����Ŀģ�漯��������Ϣ
 * <li>ͨ��F_ORGANIZATION_ID������ѡ��֯
 * 
 * @author yangjun
 *
 */
public class ProjectTemplatesOfOrg extends MasterDetailDataSetFactory {

	/**
	 * ��ȡ��Ŀģ�漯��������Ϣ,����������Ŀģ�漯�ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public ProjectTemplatesOfOrg() {
		//������Ŀģ�漯�ϵĴ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
	}

	/**
	 * ������Ŀģ�漯������֯�Ĺ����ֶΣ�{@link com.sg.business.model.WorkDefinition.F_ORGANIZATION_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinition.F_ORGANIZATION_ID;
	}
	
	
}
