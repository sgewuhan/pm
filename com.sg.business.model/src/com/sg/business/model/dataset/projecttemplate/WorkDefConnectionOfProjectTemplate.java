package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinitionConnection;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ����ǰ���ù�ϵ����
 * <p/> 
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ�ȡҵ���������ѡ��Ŀģ��Ĺ���ǰ���ùؼ�����Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ����ǰ���ùؼ���������Ϣ
 * <li>ͨ��F_PROJECT_TEMPLATE_ID������ѡ��Ŀģ��
 * 
 * @author yangjun
 *
 */
public class WorkDefConnectionOfProjectTemplate extends
		MasterDetailDataSetFactory {

	/**
	 * ��ȡ����ǰ���ù�ϵ����������Ϣ,�������ù���ǰ���ù�ϵ���ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public WorkDefConnectionOfProjectTemplate() {
		//���ù���ǰ���ù�ϵ���ϵĴ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION_CONNECTION);
	}

	/**
	 * ���ù���ǰ���ù�ϵ��������Ŀģ��Ĺ����ֶΣ�
	 * {@link com.sg.business.model.WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID;
	}

}
