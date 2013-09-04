package com.sg.business.model.dataset.message;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Message;
import com.sg.widgets.MessageUtil;

public class MyWasteMessage extends SingleDBCollectionDataSetFactory {

	public MyWasteMessage() {
			super(IModelConstants.DB, IModelConstants.C_MESSAGE);
			try {
				String userid = UserSessionContext.getAccountInfo()
						.getconsignerId();
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtil.showToast(e);
			}
		}


}
