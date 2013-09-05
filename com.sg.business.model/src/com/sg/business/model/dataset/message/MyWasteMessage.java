package com.sg.business.model.dataset.message;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Message;
import com.sg.widgets.MessageUtil;

public class MyWasteMessage extends SingleDBCollectionDataSetFactory {

	public MyWasteMessage() {
			super(IModelConstants.DB, IModelConstants.C_MESSAGE);
		}

	

	@Override
	public DBObject getQueryCondition() {
		try {
			String userid = UserSessionContext.getAccountInfo()
					.getconsignerId();
			
			BasicDBObject condition = new BasicDBObject();
			condition.put(Message.F_WASTE,new BasicDBObject().append(userid, true));
			return condition;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.showToast(e);
			return new BasicDBObject().append("_id", null);
		}
		
	}


	@Override
	public DBObject getSort() {
		return new BasicDBObject().append(Message.F_SENDDATE, -1);
	}
	
	

}
