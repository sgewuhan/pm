package com.sg.business.model.condition;

import com.mobnut.db.model.IRelationConditionProvider;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.Organization;

public class FunctionDeptByOrg implements IRelationConditionProvider {
	public FunctionDeptByOrg() {
	}
	@Override
	public DBObject getCondition(PrimaryObject primaryObject) {
		return new BasicDBObject()
		.append(Organization.F_PARENT_ID,
				primaryObject.get_id())
		.append(
				Organization.F_IS_FUNCTION_DEPARTMENT, Boolean.TRUE);
		
	}

}
