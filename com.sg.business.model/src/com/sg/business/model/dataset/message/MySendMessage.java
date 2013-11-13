package com.sg.business.model.dataset.message;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Message;
import com.sg.business.model.bson.SendDateSorter;
import com.sg.widgets.part.CurrentAccountContext;

public class MySendMessage extends SingleDBCollectionDataSetFactory {

	private String userId;


	public MySendMessage() {
		super(IModelConstants.DB, IModelConstants.C_MESSAGE);
		userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
	}

	@Override
	public DBObject getQueryCondition() {
		try {
			
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
