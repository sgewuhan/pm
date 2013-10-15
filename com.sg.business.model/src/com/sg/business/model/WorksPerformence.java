package com.sg.business.model;

import java.util.Date;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;

public class WorksPerformence extends PrimaryObject {

	public static final String F_WORKID = "workid";
	public static final String F_USERID = "userid";
	public static final String F_COMMITDATE = "commitdate";
	public static final String F_WORKS = "works";
	public static final String F_DATECODE = "datecode";
	public static final String F_PROJECTDESC = "projectdesc";
	public static final String F_WORKDESC = "workdesc";
	public static final String F_PLANWORKS = "planworks";
	public static final String F_PROJECTID = "projectid";
	
	@Override
	public boolean doSave(IContext context) throws Exception {

		Object value = getValue(F_DATECODE);
		if(!(value instanceof Long)){
			Date date = new Date();
			Long dateCode = new Long(date.getTime()/(24*60*60*1000));
			setValue(F_DATECODE, dateCode);
		}
		return super.doSave(context);
	}
	

}
