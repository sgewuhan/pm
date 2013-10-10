package com.sg.business.model.condition;

import com.mobnut.db.model.IRelationConditionProvider;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.Folder;

public class ContainerFolder implements IRelationConditionProvider {

	@Override
	public DBObject getCondition(PrimaryObject primaryObject) {
		return new BasicDBObject().append(Folder.F_PARENT_ID, null).append(
				Folder.F_ROOT_ID, primaryObject.get_id());
	}

}
