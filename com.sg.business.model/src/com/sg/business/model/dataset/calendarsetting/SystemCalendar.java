package com.sg.business.model.dataset.calendarsetting;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.CalendarSetting;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.bson.SEQSorter;

/**
 * <p>
 * 系统日历
 * <p/>
 * 继承于 {@link com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory}，
 * 用于获取系统日历信息<br/>
 * 实现以下几种功能：
 * <li>获取系统日历集合数据信息
 * <li>设置日历排序方式
 * 
 * @author yangjun
 * 
 */
public class SystemCalendar extends SingleDBCollectionDataSetFactory {

	/**
	 * 系统日历构造函数,用于设置系统日历的存放数据库及数据存储表
	 */
	public SystemCalendar() {
		//设置系统日历的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_CALENDAR_SETTING);
		//设置独立工作定义的显示条件：
		//CalendarSetting.F_PROJECT_ID为NULL的数据
		setQueryCondition(new BasicDBObject().append(CalendarSetting.F_PROJECT_ID, null));
	}

	/**
	 * 设置系统日历的排序方式，使用默认的seq字段进行排序
	 */
	@Override
	public DBObject getSort() {
		return new SEQSorter().getBSON();
	}

}
