package com.sg.business.model.bson;

import com.mobnut.db.model.IBSONProvider;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.Message;

public class SendDateSorter implements IBSONProvider {
	
	@Override
	public DBObject getBSON() {
		return new BasicDBObject().append(Message.F__ID, -1);
	}

}
