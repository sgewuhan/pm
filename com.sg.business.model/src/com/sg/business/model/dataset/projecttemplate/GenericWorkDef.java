package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * �������弯��
 * <p/> 
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ�ȡҵ���������ѡ��Ŀģ��Ĺ������弯����Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ�������弯��������Ϣ
 * <li>ͨ��id������ѡ��Ŀģ��
 * 
 * @author yangjun
 *
 */
public class GenericWorkDef extends MasterDetailDataSetFactory {

	/**
	 * ��ȡ�������弯��������Ϣ,�������ù������弯�ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public GenericWorkDef() {
		//���ù������弯�ϵĴ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
	}

	/**
	 * ���ù������弯������Ŀģ��Ĺ����ֶΣ�{@link com.sg.business.model.WorkDefinition.F__ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinition.F__ID;
	}

}
