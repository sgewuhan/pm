package com.sg.business.model.dataset.calendarsetting;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.CalendarSetting;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProjectCalendar extends MasterDetailDataSetFactory implements IWorkingTimeCaculator{


	public ProjectCalendar() {
		super(IModelConstants.DB, IModelConstants.C_CALENDAR_SETTING);
		setSort(new SEQSorter().getBSON());
	}

	@Override
	protected String getDetailCollectionKey() {
		return CalendarSetting.F_PROJECT_ID;
	}

	public double getWorkingTime(Date date){
		String sdate = new SimpleDateFormat("yyyyMMdd").format(date);
		return getWorkingTime(sdate);
	}
	
	public double getWorkingTime(String date){
		List<PrimaryObject> di = getDataSet().getDataItems();
		for (PrimaryObject po : di) {
			CalendarSetting cs = (CalendarSetting) po;
			Double value = cs.getCalendarWorkingTime(date);
			if(value!=null){
				return value.doubleValue();
			}
		}
		return 0d;
	}

	@Override
	public int getWorkingDays(Date startDate, Date endDate) {
		int count = 0;
		Calendar sdate = Utils.getDayBegin(startDate);
		Calendar edate = Utils.getDayEnd(endDate);
		
		Calendar current = sdate;
		while(current.before(edate)){
			Date d = current.getTime();
			double t = getWorkingTime(d);
			if(t!=0d){
				count++;
			}
			current.set(Calendar.DATE, current.get(Calendar.DATE) + 1);
		}
		return count;
	}

}
