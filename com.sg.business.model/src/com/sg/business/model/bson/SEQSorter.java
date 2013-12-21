package com.sg.business.model.bson;

import com.mobnut.db.model.IBSONProvider;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SEQSorter implements IBSONProvider {
	
	private int i = 1;
	
	public SEQSorter() {
	}

	public SEQSorter(int i) {
		this.i = i;
	}

	@Override
	public DBObject getBSON() {
		return new BasicDBObject().append("seq", i); //$NON-NLS-1$
	}

}
