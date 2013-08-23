package com.sg.business.model.dataset.calendarsetting;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.CalendarSetting;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.bson.SEQSorter;

public class SystemCalendar extends SingleDBCollectionDataSetFactory implements IWorkingTimeCaculator{

	public SystemCalendar() {
		super(IModelConstants.DB, IModelConstants.C_CALENDAR_SETTING);
		setQueryCondition(new BasicDBObject().append(CalendarSetting.F_PROJECT_ID, null));
	}

	@Override
	public DBObject getSort() {
		return new SEQSorter().getBSON();
	}

	@Override
	public double getWorkingTime(Date date){
		String sdate = new SimpleDateFormat("yyyyMMdd").format(date);
		return getWorkingTime(sdate);
	}
	
	@Override
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
	public int getWorkingDays(Date startDate,Date endDate) {
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
