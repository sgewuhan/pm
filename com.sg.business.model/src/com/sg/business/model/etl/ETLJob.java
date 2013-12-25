package com.sg.business.model.etl;

import java.util.Calendar;

import com.mongodb.BasicDBObject;
import com.sg.business.model.Project;

public class ETLJob extends AbstractETLJob{
	
	@Override
	protected int getMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.get(Calendar.MONTH) + 1;
	}

	@Override
	protected int getYear() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.get(Calendar.YEAR);
	}

	@Override
	protected int getDay() {
		return Calendar.getInstance().get(Calendar.DATE);
	}
	
	@Override
	protected BasicDBObject getProjectETLQuery() {
		BasicDBObject query = new BasicDBObject();
		query.put(Project.F_LIFECYCLE,
				new BasicDBObject()
						.append("$in", new String[] { //$NON-NLS-1$
								Project.STATUS_CANCELED_VALUE,
								Project.STATUS_FINIHED_VALUE,
								Project.STATUS_PAUSED_VALUE,
								Project.STATUS_WIP_VALUE }));
		return query;
	}


}
