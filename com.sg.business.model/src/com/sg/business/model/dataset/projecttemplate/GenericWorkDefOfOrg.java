package com.sg.business.model.dataset.projecttemplate;

import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ͨ�ù������弯��
 * <p/> 
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ�ȡҵ���������ѡ��֯��ͨ�ù������弯����Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡͨ�ù������弯��������Ϣ
 * <li>ͨ��F_ORGANIZATION_ID������ѡ��֯
 * 
 * @author yangjun
 *
 */
public class GenericWorkDefOfOrg extends
		MasterDetailDataSetFactory {

	/**
	 * ��ȡͨ�ù������弯��������Ϣ,��������ͨ�ù������弯�ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public GenericWorkDefOfOrg() {
		//����ͨ�ù������弯�ϵĴ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
		//����ͨ�ù����������ʾ������
		//WorkDefinition.F_WORK_TYPEΪWorkDefinition.WORK_TYPE_GENERIC
		//WorkDefinition.F_PARENT_IDΪNUll������
		setQueryCondition(new BasicDBObject().append(
				WorkDefinition.F_WORK_TYPE, WorkDefinition.WORK_TYPE_GENERIC)
				.append(WorkDefinition.F_PARENT_ID, null));
	}

	/**
	 * ����ͨ�ù������弯������֯�Ĺ����ֶΣ�{@link com.sg.business.model.WorkDefinition.F_ORGANIZATION_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinition.F_ORGANIZATION_ID;
	}
}
