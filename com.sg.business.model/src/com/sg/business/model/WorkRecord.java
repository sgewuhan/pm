package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class WorkRecord extends PrimaryObject {

	
	// public static final String F_USERID = "userid";
	// public static final String F_RDATE = "rdate";
	// public static final String F_PERCENT = "percent";
	// public static final String F_WORK_ID = "work_id";
	//
	// @Override
	// public void doInsert(IContext context) throws Exception {
	// ObjectId workid=(ObjectId) getValue(F_WORK_ID);
	// Work work=ModelService.createModelObject(Work.class, workid);
	// BasicDBList dataList = (BasicDBList) work.getValue(Work.F_RECORD);
	// if (dataList == null) {
	// dataList = new BasicDBList();
	// }
	//
	// BasicDBObject record=new BasicDBObject();
	// record.put(F_USERID,getValue(F_USERID));
	// record.put(F_RDATE, getValue(F_RDATE));
	// record.put(F_PERCENT, getValue(F_PERCENT));
	// record.put(F_DESC, getValue(F_DESC));
	// dataList.add(record);
	//
	// DBCollection col = DBActivator.getCollection(IModelConstants.DB,
	// IModelConstants.C_WORK);
	// col.update(new BasicDBObject().append("_id", getValue(F_WORK_ID)),new
	// BasicDBObject().append("$set",
	// new BasicDBObject().append(Work.F_RECORD,dataList)));
	//
	// }

	public static final String F_WORK_ID = "work_id";

	@Override
	public void doInsert(IContext context) throws Exception {
		ObjectId workId = (ObjectId) getValue(F_WORK_ID);

		DBCollection col = getCollection(IModelConstants.C_WORK);

		col.update(
				new BasicDBObject().append(Work.F__ID, workId),
				new BasicDBObject().append("$push",
						new BasicDBObject().append(Work.F_RECORD, get_data())));
	}
}
