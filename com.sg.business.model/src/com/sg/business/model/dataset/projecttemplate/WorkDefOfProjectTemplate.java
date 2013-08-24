package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * �����ﶨ�弯��
 * <p/> 
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ�ȡҵ���������ѡ��Ŀģ��Ľ����ﶨ�弯����Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ�����ﶨ�弯��������Ϣ
 * <li>ͨ��F__ID������ѡ��Ŀģ��
 * <li>������Ŀģ��Ĺ����ֶ�F_WORK_DEFINITON_ID
 * 
 * @author yangjun
 *
 */
public class WorkDefOfProjectTemplate extends MasterDetailDataSetFactory {

	/**
	 * �����ﶨ�弯�Ϲ��캯��,�������ý����ﶨ�弯�ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public WorkDefOfProjectTemplate() {
		//���ý����ﶨ�弯�ϵĴ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
		//���ý����ﶨ�弯�ϵ�����ʽ��ʹ��Ĭ�ϵ�seq�ֶν�������
		setSort(new SEQSorter().getBSON());
	}

	/**
	 * ���ý����ﶨ�弯������Ŀģ��Ĺ����ֶΣ�{@link com.sg.business.model.WorkDefinition.F__ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinition.F__ID;
	}

	/**
	 * �ı��ȡ��Ŀģ�������ֵ��{@link com.sg.business.model.ProjectTemplate.F_WORK_DEFINITON_ID }
	 */
	@Override
	protected Object getMasterValue() {
		return master.getValue(ProjectTemplate.F_WORK_DEFINITON_ID);
	}
	
	
}
