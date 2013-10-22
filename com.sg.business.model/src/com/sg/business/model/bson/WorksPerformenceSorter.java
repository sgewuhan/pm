package com.sg.business.model.bson;

import com.mobnut.db.model.IBSONProvider;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.WorksPerformence;

public class WorksPerformenceSorter implements IBSONProvider {

	@Override
	public DBObject getBSON() {
		return new BasicDBObject().append(WorksPerformence.F_DATECODE, 1);
	}

}
