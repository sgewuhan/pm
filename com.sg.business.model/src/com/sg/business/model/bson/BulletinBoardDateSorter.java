package com.sg.business.model.bson;

import com.mobnut.db.model.IBSONProvider;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.BulletinBoard;

public class BulletinBoardDateSorter implements IBSONProvider {

	@Override
	public DBObject getBSON() {
		return new BasicDBObject().append(BulletinBoard.F_PUBLISH_DATE, -1);
	}

}
