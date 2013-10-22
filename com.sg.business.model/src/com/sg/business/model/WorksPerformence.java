package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.graphics.Image;

import com.sg.business.resource.BusinessResource;

public class WorksPerformence extends AbstractWorksMetadata {

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_LOG_16);
	}
	
//	@Override
//	public String getLabel() {
//		return getDesc()+" "+getLogDate();
//		
//	}

	public String getLogDate() {
		Long dateCode = (Long) getValue(F_DATECODE);
		if(dateCode!=null){
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(dateCode.longValue()*24*60*60*1000);
			Date date = cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date);
		}
		return null;
	}
}
