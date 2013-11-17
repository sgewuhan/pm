package com.sg.business.model.condition;

import com.mobnut.db.model.IRelationConditionProvider;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.Document;

public class ReleasedDoc implements IRelationConditionProvider {

	public ReleasedDoc() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public DBObject getCondition(PrimaryObject primaryObject) {
		return new BasicDBObject()
		.append(Document.F_FOLDER_ID,
				primaryObject.get_id())
		.append(Document.F_LIFECYCLE,
				Document.STATUS_RELEASED_ID);
	}

}
