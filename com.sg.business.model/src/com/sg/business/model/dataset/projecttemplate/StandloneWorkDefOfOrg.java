package com.sg.business.model.dataset.projecttemplate;

import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * �����������弯��
 * <p/> 
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ�ȡҵ���������ѡ��֯�Ķ����������弯����Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ�����������弯��������Ϣ
 * <li>ͨ��F_ORGANIZATION_ID������ѡ��֯
 * 
 * @author yangjun
 *
 */
public class StandloneWorkDefOfOrg extends
		MasterDetailDataSetFactory {

	/**
	 * ��ȡ�����������弯��������Ϣ,�������ö����������弯�ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public StandloneWorkDefOfOrg() {
		//���ö����������弯�ϵĴ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
		//���ö��������������ʾ������
		//WorkDefinition.F_WORK_TYPEΪWorkDefinition.WORK_TYPE_STANDLONE
		//WorkDefinition.F_PARENT_IDΪNUll������
		setQueryCondition(new BasicDBObject().append(
				WorkDefinition.F_WORK_TYPE, WorkDefinition.WORK_TYPE_STANDLONE)
				.append(WorkDefinition.F_PARENT_ID, null));
	}

	/**
	 * ���ö����������弯������֯�Ĺ����ֶΣ�{@link com.sg.business.model.WorkDefinition.F_ORGANIZATION_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinition.F_ORGANIZATION_ID;
	}

}
