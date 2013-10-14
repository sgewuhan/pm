package com.sg.business.work.view;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IWorksSummary;

public class DateSummaryLabelProvider extends ColumnLabelProvider {

	private int currentYear;
	private int month;
	private int dayOfMonth;
	private HashMap<ObjectId,PrimaryObject> cache;

	public DateSummaryLabelProvider(int currentYear, int month,int dayOfMonth) {
		this.currentYear = currentYear;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
	}
	
	@Override
	public String getText(Object element) {
		IWorksSummary ws = ((PrimaryObject)element).getAdapter(IWorksSummary.class);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, currentYear);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, dayOfMonth);
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date date = cal.getTime();
		
		double summary = ws.getWorksSummaryOfDay(date,cache);
		if(summary == 0){
			return "";
		}else{
			DecimalFormat df = new DecimalFormat(Utils.NF_NUMBER_P2);
			return df.format(summary);
		}
	}

	public void setCache(HashMap<ObjectId,PrimaryObject> cache) {
		this.cache = cache;
	}


}
