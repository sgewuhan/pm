package com.sg.business.model.dataset.message;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Message;
import com.sg.business.model.bson.SendDateSorter;

public class MySendMessage extends SingleDBCollectionDataSetFactory {

	public MySendMessage() {
		super(IModelConstants.DB, IModelConstants.C_MESSAGE);
	}

	@Override
	public DBObject getQueryCondition() {
		try {
			String userId = getContext().getAccountInfo().getConsignerId();
			
			BasicDBObject condition = new BasicDBObject();
			condition.put(Message.F_SENDER,userId);
			condition.put(Message.F_WASTE+"."+userId,new BasicDBObject().append("$ne", true));
			return condition;
		} catch (Exception e) {
			return new BasicDBObject().append("_id", null);
		}
		
	}


	@Override
	public DBObject getSort() {
		return new SendDateSorter().getBSON();
	}
	
	

}
