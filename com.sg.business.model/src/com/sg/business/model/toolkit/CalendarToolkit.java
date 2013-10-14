package com.sg.business.model.toolkit;

import java.util.Date;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.dataset.calendarsetting.CalendarCaculater;
import com.sg.business.model.dataset.calendarsetting.SystemCalendar;

public class CalendarToolkit {

	public static double getWorkingHours(Date date){
		List<PrimaryObject> items = new SystemCalendar().getDataSet().getDataItems();
		return new CalendarCaculater(items).getWorkingTime(date);
	}
}
