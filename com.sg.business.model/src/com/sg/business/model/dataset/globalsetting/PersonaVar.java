package com.sg.business.model.dataset.globalsetting;

import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.VariableSetting;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.MessageUtil;

public class PersonaVar extends SingleDBCollectionDataSetFactory{

	public PersonaVar() {
		super(IModelConstants.DB, IModelConstants.C_VARIABLESETTING);
	}
	
	@Override
	public DBObject getQueryCondition() {
		try {
			AccountInfo account = UserSessionContext.getAccountInfo();
			String userid = account.getConsignerId();
			DBObject queryCondition = new BasicDBObject();
			queryCondition.put(VariableSetting.F_PERSONAL, userid);
			return queryCondition;

		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append(VariableSetting.F__ID, null);
		}
	}
	

}
