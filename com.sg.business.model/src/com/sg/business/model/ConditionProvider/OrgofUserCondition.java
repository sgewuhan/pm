package com.sg.business.model.ConditionProvider;

import com.mobnut.db.model.IRelationConditionProvider;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.User;

public class OrgofUserCondition implements IRelationConditionProvider {

	@Override
	public DBObject getCondition() {
		return new BasicDBObject().append(mr.getEnd2Key(), getValue(mr.getEnd1Key())).append(User.F_ACTIVATED, Boolean.TRUE);
	}

}
