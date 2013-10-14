package com.sg.business.work.view;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IWorksSummary;

public class MonthSummaryLabelProvider extends ColumnLabelProvider {

	private int currentYear;
	private int month;

	public MonthSummaryLabelProvider(int currentYear, int month) {
		this.currentYear = currentYear;
		this.month = month;
	}
	
	@Override
	public String getText(Object element) {
		IWorksSummary ws = ((PrimaryObject)element).getAdapter(IWorksSummary.class);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, currentYear);
		cal.set(Calendar.MONTH, month);
		//得到该月第一天
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		Date start = cal.getTime();
		
		//得到最后一天
		int day = cal.getActualMaximum(Calendar.DATE);
		cal.set(Calendar.DATE, day);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		Date end = cal.getTime();
		double summary = ws.getWorksSummary(start,end);
		if(summary == 0){
			return "";
		}else{
			DecimalFormat df = new DecimalFormat("#########");
			return df.format(summary);
		}
	}


}
