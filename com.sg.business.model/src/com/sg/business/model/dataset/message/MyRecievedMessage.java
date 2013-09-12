package com.sg.business.model.dataset.message;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Message;
import com.sg.widgets.MessageUtil;

public class MyRecievedMessage extends SingleDBCollectionDataSetFactory {

	public MyRecievedMessage() {
		super(IModelConstants.DB, IModelConstants.C_MESSAGE);
	}
	
	@Override
	public DBObject getQueryCondition() {
		try {
			String userid = UserSessionContext.getAccountInfo()
					.getConsignerId();
			
			BasicDBObject condition = new BasicDBObject();
			condition.put(Message.F_RECIEVER,userid);
			condition.put(Message.F_WASTE+"."+userid,new BasicDBObject().append("$ne", true));
			return condition;
		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append("_id", null);
		}
		
	}


	@Override
	public DBObject getSort() {
		return new BasicDBObject().append(Message.F_SENDDATE, -1);
	}
	

}
