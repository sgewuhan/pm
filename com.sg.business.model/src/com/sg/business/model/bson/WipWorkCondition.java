package com.sg.business.model.bson;

import com.mobnut.db.model.IRelationConditionProvider;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.Work;

public class WipWorkCondition implements IRelationConditionProvider {

	public WipWorkCondition() {
	}

	@Override
	public DBObject getCondition(PrimaryObject primaryObject) {
		return new BasicDBObject().append(Work.F_PARENT_ID, primaryObject.get_id()).append(Work.F_LIFECYCLE, Work.STATUS_WIP_VALUE);
	}

}
