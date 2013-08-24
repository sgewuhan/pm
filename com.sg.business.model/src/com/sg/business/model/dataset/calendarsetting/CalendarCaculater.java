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
 * ����������
 * </p>
 * 
 * <li>
 * 
 * @author yangjun
 * 
 */
public class CalendarCaculater {

	/**
	 * ϵͳ����Ŀ�����������õĹ����պ���Ϣ������
	 */
	private List<PrimaryObject> conditions;

	/**
	 * ���캯����ͨ��ϵͳ����Ŀ�����������õĹ����պ���Ϣ����������
	 * 
	 * @param conditions
	 *            �� ϵͳ����Ŀ�����������õĹ����պ���Ϣ������
	 */
	public CalendarCaculater(List<PrimaryObject> conditions) {
		this.conditions = conditions;
	}

	/**
	 * ���캯����ͨ����ĿID����
	 * 
	 * @param projectId
	 *            ����ĿID
	 */
	public CalendarCaculater(ObjectId projectId) {
		// ��ȡ��Ŀ��Ϣ
		Project project = ModelService.createModelObject(
				new BasicDBObject().append(Project.F__ID, projectId),
				Project.class);
		// ��ȡ��Ŀ���������õĹ����պ���Ϣ������
		conditions = project.getCalendarCondition();
	}

	/**
	 * ��ȡ����ʱ�䣨Сʱ��
	 * 
	 * @param date
	 *            ��Date���͵�ʱ������
	 * @return ����ʱ�䣨Сʱ��
	 */
	public double getWorkingTime(Date date) {
		// �������ʱ��ת���ɡ�yyyyMMdd����ʽ���ַ���
		String sdate = new SimpleDateFormat("yyyyMMdd").format(date);
		return getWorkingTime(sdate);
	}

	/**
	 * ��ȡ����ʱ�䣨Сʱ��
	 * 
	 * @param date
	 *            ��String���͵�ʱ������
	 * @return ����ʱ�䣨Сʱ��
	 */
	public double getWorkingTime(String date) {
		// ѭ���жϵ�ǰ�����Ƿ������õ������У��Ӷ���ȡ����ʱ����Ϣ
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
	 * ��ȡ����ʱ�䣨�죩
	 * 
	 * @param startDate
	 *            �� ��ʼ����
	 * @param endDate
	 *            �� ��ֹ����
	 * @return ����ʱ�䣨�죩
	 */
	public int getWorkingDays(Date startDate, Date endDate) {
		int count = 0;
		Calendar sdate = Utils.getDayBegin(startDate);
		Calendar edate = Utils.getDayEnd(endDate);

		// ѭ�����㹤��ʱ������
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
