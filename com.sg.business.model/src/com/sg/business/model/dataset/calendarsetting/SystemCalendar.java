package com.sg.business.model.dataset.calendarsetting;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.CalendarSetting;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.bson.SEQSorter;

/**
 * <p>
 * ϵͳ����
 * <p/>
 * �̳��� {@link com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory}��
 * ���ڻ�ȡϵͳ������Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡϵͳ��������������Ϣ
 * <li>������������ʽ
 * 
 * @author yangjun
 * 
 */
public class SystemCalendar extends SingleDBCollectionDataSetFactory {

	/**
	 * ϵͳ�������캯��,��������ϵͳ�����Ĵ�����ݿ⼰���ݴ洢��
	 */
	public SystemCalendar() {
		//����ϵͳ�����Ĵ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_CALENDAR_SETTING);
		//���ö��������������ʾ������
		//CalendarSetting.F_PROJECT_IDΪNULL������
		setQueryCondition(new BasicDBObject().append(CalendarSetting.F_PROJECT_ID, null));
	}

	/**
	 * ����ϵͳ����������ʽ��ʹ��Ĭ�ϵ�seq�ֶν�������
	 */
	@Override
	public DBObject getSort() {
		return new SEQSorter().getBSON();
	}

}
