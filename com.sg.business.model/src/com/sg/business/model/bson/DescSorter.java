package com.sg.business.model.bson;

import com.mobnut.db.model.IBSONProvider;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DescSorter implements IBSONProvider {
	@Override
	public DBObject getBSON() {
		return new BasicDBObject().append(PrimaryObject.F_DESC, 0);
	}

}
