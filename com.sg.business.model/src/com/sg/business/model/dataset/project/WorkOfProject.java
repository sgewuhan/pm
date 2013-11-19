package com.sg.business.model.dataset.project;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ��Ŀ��������
 * </p>
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ����Ŀ�����ļ�����Ϣ��Ӧ�����½���Ŀ��WBS��ʾ<br/>
 * �������¹��ܣ�
 * <li>��ȡ��Ŀ��������������Ϣ
 * <li>ͨ��F__ID������ѡ��Ŀ
 * <li>������Ŀ�Ĺ����ֶ�F_WORK_ID
 * 
 * @author yangjun
 *
 */
public class WorkOfProject extends MasterDetailDataSetFactory {

	/**
	 * ��Ŀ�����ļ��Ϲ��캯��,����������Ŀ�����ļ��ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public WorkOfProject() {
		//������Ŀ�����ļ��ϵĴ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_WORK);
		//������Ŀ�������ϵ�����ʽ��ʹ��Ĭ�ϵ�seq�ֶν�������
		setSort(new SEQSorter().getBSON());
	}

	/**
	 * ������Ŀ������������Ŀ�Ĺ����ֶΣ�{@link com.sg.business.model.Work.F__ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Work.F__ID;
	}


	/**
	 * �ı��ȡ��Ŀ������ֵ��{@link com.sg.business.model.VisProject.F_WORK_ID }
	 */
	@Override
	protected Object getMasterValue() {
		return master.getValue(Project.F_WORK_ID);
	}
	
	
}
