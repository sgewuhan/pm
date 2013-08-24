package com.sg.business.model.dataset.calendarsetting;

import com.sg.business.model.CalendarSetting;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 项目日历
 * <p/>
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获取项目项下的项目日历信息<br/>
 * 实现以下几种功能：
 * <li>获取项目日历集合数据信息
 * <li>通过F_PROJECT_ID关联项目
 * 
 * @author yangjun
 * 
 */
public class ProjectCalendar extends MasterDetailDataSetFactory {

	/**
	 * 项目日历构造函数,用于设置项目日历的存放数据库及数据存储表
	 */
	public ProjectCalendar() {
		//设置项目日历的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_CALENDAR_SETTING);
		//设置项目日历的排序方式，使用默认的seq字段进行排序
		setSort(new SEQSorter().getBSON());
	}

	/**
	 * 设置项目日历与项目的关联字段：{@link com.sg.business.model.CalendarSetting.F_PROJECT_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return CalendarSetting.F_PROJECT_ID;
	}

}
