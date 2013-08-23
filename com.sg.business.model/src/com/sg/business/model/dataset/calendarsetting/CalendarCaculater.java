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

public class CalendarCaculater {

	private List<PrimaryObject> conditions;

	public CalendarCaculater(List<PrimaryObject> conditions) {
		this.conditions = conditions;
	}

	public CalendarCaculater(ObjectId projectId) {
		Project project = ModelService.createModelObject(
				new BasicDBObject().append(Project.F__ID, projectId),
				Project.class);
		conditions = project.getCalendarCondition();
	}

	public double getWorkingTime(Date date) {
		String sdate = new SimpleDateFormat("yyyyMMdd").format(date);
		return getWorkingTime(sdate);
	}

	public double getWorkingTime(String date) {
		for (PrimaryObject po : conditions) {
			CalendarSetting cs = (CalendarSetting) po;
			Double value = cs.getCalendarWorkingTime(date);
			if (value != null) {
				return value.doubleValue();
			}
		}
		return 0d;
	}

	public int getWorkingDays(Date startDate, Date endDate) {
		int count = 0;
		Calendar sdate = Utils.getDayBegin(startDate);
		Calendar edate = Utils.getDayEnd(endDate);

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
