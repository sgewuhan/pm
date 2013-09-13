package com.sg.business.model.dataset.project;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ��������
 * </p>
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ�ù����ļ�����Ϣ��Ӧ�����½�������WBS��ʾ<br/>
 * �������¹��ܣ�
 * <li>��ȡ��������������Ϣ
 * <li>ͨ��F__ID������ѡ����
 * <li>���ù����ֶ�F__ID
 * 
 * @author yangjun
 *
 */
public class WorkOfWork extends MasterDetailDataSetFactory {

	/**
	 * �����ļ��Ϲ��캯��,�������ù����ļ��ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public WorkOfWork() {
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
	 * �ı��ȡ��Ŀ������ֵ��{@link com.sg.business.model.Work.F__ID }
	 */
	@Override
	protected Object getMasterValue() {
		return master.getValue(Work.F__ID);
	}
	
	
}
