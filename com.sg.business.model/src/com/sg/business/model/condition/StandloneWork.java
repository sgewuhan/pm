package com.sg.business.model.condition;

import com.mobnut.db.model.IRelationConditionProvider;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.WorkDefinition;

public class StandloneWork implements IRelationConditionProvider {

	@Override
	public DBObject getCondition(PrimaryObject primaryObject) {
		return new BasicDBObject()
				.append(WorkDefinition.F_ORGANIZATION_ID,
						primaryObject.get_id())
				.append(WorkDefinition.F_WORK_TYPE,
						WorkDefinition.WORK_TYPE_STANDLONE)
				.append(WorkDefinition.F_PARENT_ID, null);
	}

}
