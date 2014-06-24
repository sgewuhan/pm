package com.sg.business.model.dataset.message;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Message;
import com.sg.business.model.bson.SendDateSorter;
import com.sg.widgets.part.CurrentAccountContext;

public class MyRecievedMessage extends SingleDBCollectionDataSetFactory {

	private String userId;


	public MyRecievedMessage() {
		super(IModelConstants.DB, IModelConstants.C_MESSAGE);
		userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
	}
	
	@Override
	public DBObject getQueryCondition() {
		try {
			DBObject condition = createQueryCondition();
			condition.put(Message.F_RECIEVER,userId);
			condition.put(Message.F_WASTE+"."+userId,new BasicDBObject().append("$ne", true)); //$NON-NLS-1$ //$NON-NLS-2$
			return condition;
		} catch (Exception e) {
			return new BasicDBObject().append("_id", null); //$NON-NLS-1$
		}
		
	}


	@Override
	public DBObject getSort() {
		return new SendDateSorter().getBSON();
	}
	

}
