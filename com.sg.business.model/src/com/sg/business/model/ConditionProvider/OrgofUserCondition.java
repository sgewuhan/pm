package com.sg.business.model.ConditionProvider;

import com.mobnut.db.model.IRelationConditionProvider;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.Organization;
import com.sg.business.model.User;

public class OrgofUserCondition implements IRelationConditionProvider {

	@Override
	public DBObject getCondition(PrimaryObject po) {

		return new BasicDBObject().append(User.F_ORGANIZATION_ID,
				po.getValue(Organization.F__ID)).append(User.F_ACTIVATED,
				Boolean.TRUE);
	}

}
