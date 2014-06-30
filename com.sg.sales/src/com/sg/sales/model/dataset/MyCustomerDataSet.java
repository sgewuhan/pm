package com.sg.sales.model.dataset;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.TeamControl;
import com.sg.widgets.commons.dataset.ContextSingleDataSetFactory;

public class MyCustomerDataSet extends ContextSingleDataSetFactory{

	public MyCustomerDataSet() {
		super(IModelConstants.DB, Sales.C_COMPANY);
	}
	
	@Override
	public DBObject getQueryCondition() {
		String userid = getContext().getAccountInfo().getConsignerId();
		DBObject query = createQueryCondition();
		BasicDBObject teamCondition = TeamControl.getVisitableCondition(userid);
		teamCondition.putAll(query);
		return teamCondition;
	}

}
