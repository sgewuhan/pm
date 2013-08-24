package com.sg.business.model.dataset.calendarsetting;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.CalendarSetting;
import com.sg.business.model.Project;

/**
 * <p>
 * 日历计算器
 * </p>
 * 
 * <li>
 * 
 * @author yangjun
 * 
 */
public class CalendarCaculater {

	/**
	 * 系统（项目）日历中设置的工作日和休息日条件
	 */
	private List<PrimaryObject> conditions;

	/**
	 * 构造函数，通过系统（项目）日历中设置的工作日和休息日条件构造
	 * 
	 * @param conditions
	 *            ： 系统（项目）日历中设置的工作日和休息日条件
	 */
	public CalendarCaculater(List<PrimaryObject> conditions) {
		this.conditions = conditions;
	}

	/**
	 * 构造函数，通过项目ID构造
	 * 
	 * @param projectId
	 *            ：项目ID
	 */
	public CalendarCaculater(ObjectId projectId) {
		// 获取项目信息
		Project project = ModelService.createModelObject(
				new BasicDBObject().append(Project.F__ID, projectId),
				Project.class);
		// 获取项目日历中设置的工作日和休息日条件
		conditions = project.getCalendarCondition();
	}

	/**
	 * 获取工作时间（小时）
	 * 
	 * @param date
	 *            ：Date类型的时间数据
	 * @return 工作时间（小时）
	 */
	public double getWorkingTime(Date date) {
		// 把输入的时间转换成“yyyyMMdd”格式的字符串
		String sdate = new SimpleDateFormat("yyyyMMdd").format(date);
		return getWorkingTime(sdate);
	}

	/**
	 * 获取工作时间（小时）
	 * 
	 * @param date
	 *            ：String类型的时间数据
	 * @return 工作时间（小时）
	 */
	public double getWorkingTime(String date) {
		// 循环判断当前日期是否在设置的条件中，从而获取工作时间信息
		for (PrimaryObject po : conditions) {
			CalendarSetting cs = (CalendarSetting) po;
			Double value = cs.getCalendarWorkingTime(date);
			if (value != null) {
				return value.doubleValue();
			}
		}
		return 0d;
	}

	/**
	 * 获取工作时间（天）
	 * 
	 * @param startDate
	 *            ： 起始日期
	 * @param endDate
	 *            ： 终止日期
	 * @return 工作时间（天）
	 */
	public int getWorkingDays(Date startDate, Date endDate) {
		int count = 0;
		Calendar sdate = Utils.getDayBegin(startDate);
		Calendar edate = Utils.getDayEnd(endDate);

		// 循环计算工作时间天数
		Calendar current = sdate;
		while (current.before(edate)) {
			Date d = current.getTime();
			double t = getWorkingTime(d);
			if (t != 0d) {
				count++;
			}
			current.set(Calendar.DATE, current.get(Calendar.DATE) + 1);
		}
		return count;
	}

}
