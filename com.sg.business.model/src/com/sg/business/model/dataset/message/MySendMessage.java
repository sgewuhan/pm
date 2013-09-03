package com.sg.business.model.dataset.message;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Message;
import com.sg.widgets.MessageUtil;

public class MySendMessage extends SingleDBCollectionDataSetFactory {

	public MySendMessage() {
		super(IModelConstants.DB, IModelConstants.C_MESSAGE);
		try {
			String userid = UserSessionContext.getAccountInfo()
					.getconsignerId();
			setQueryCondition(new BasicDBObject().append(Message.F_SENDER,
					userid).append(Message.F_PARENT_MESSAGE, null));
			setSort(new BasicDBObject().append(Message.F_SENDDATE, -1));
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.showToast(e);
		}
	}

}
