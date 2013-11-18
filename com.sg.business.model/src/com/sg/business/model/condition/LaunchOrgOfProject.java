package com.sg.business.model.condition;

import java.util.List;

import com.mobnut.db.model.IRelationConditionProvider;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;

public class LaunchOrgOfProject implements IRelationConditionProvider {

	public LaunchOrgOfProject() {
	}

	@Override
	public DBObject getCondition(PrimaryObject primaryObject) {
		Object ids = primaryObject.getValue(Project.F_LAUNCH_ORGANIZATION);
		if((ids instanceof List<?>)||(ids instanceof Object[])){
			BasicDBObject query = new BasicDBObject();
			query.put(Organization.F__ID, new BasicDBObject().append("$in", ids));
			return query;
		}
		return new BasicDBObject().append(Organization.F__ID, null);
	}

}
