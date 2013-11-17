package com.sg.business.model.condition;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.WorkDefinition;

public class InternalByStandloneWork extends StandloneWork {

	public InternalByStandloneWork() {
		
	}
@Override
public DBObject getCondition(PrimaryObject primaryObject) {
	return ((BasicDBObject)super.getCondition(primaryObject)).append(WorkDefinition.F_INTERNAL_TYPE,WorkDefinition.INTERNAL_TYPE_CHANGERANGE);
}
}
