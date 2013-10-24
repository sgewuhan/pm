package com.sg.business.model.dataset.finance;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;

public class FinanceOrganizationDataSet extends
		SingleDBCollectionDataSetFactory {

	public FinanceOrganizationDataSet() {
		super(IModelConstants.DB,IModelConstants.C_ORGANIZATION);
	}

	
	@Override
	public DBObject getQueryCondition() {
		// TODO Auto-generated method stub
		return super.getQueryCondition();
	}
	
}
