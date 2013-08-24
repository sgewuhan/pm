package com.sg.business.model.dataset.calendarsetting;

import com.sg.business.model.CalendarSetting;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ��Ŀ����
 * <p/>
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ�ȡ��Ŀ���µ���Ŀ������Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ��Ŀ��������������Ϣ
 * <li>ͨ��F_PROJECT_ID������Ŀ
 * 
 * @author yangjun
 * 
 */
public class ProjectCalendar extends MasterDetailDataSetFactory {

	/**
	 * ��Ŀ�������캯��,����������Ŀ�����Ĵ�����ݿ⼰���ݴ洢��
	 */
	public ProjectCalendar() {
		//������Ŀ�����Ĵ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_CALENDAR_SETTING);
		//������Ŀ����������ʽ��ʹ��Ĭ�ϵ�seq�ֶν�������
		setSort(new SEQSorter().getBSON());
	}

	/**
	 * ������Ŀ��������Ŀ�Ĺ����ֶΣ�{@link com.sg.business.model.CalendarSetting.F_PROJECT_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return CalendarSetting.F_PROJECT_ID;
	}

}
