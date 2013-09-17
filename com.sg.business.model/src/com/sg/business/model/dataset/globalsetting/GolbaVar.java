package com.sg.business.model.dataset.globalsetting;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.VariableSetting;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.MessageUtil;

public class GolbaVar extends SingleDBCollectionDataSetFactory {

	public GolbaVar() {
		super(IModelConstants.DB, IModelConstants.C_VARIABLESETTING);
	}

	
	@Override
	public DBObject getQueryCondition() {
		try {
			DBObject queryCondition = new BasicDBObject();
			queryCondition.put(VariableSetting.F_PERSONAL, null);
			return queryCondition;
		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append(VariableSetting.F__ID, null);
		}
	}
	

}
