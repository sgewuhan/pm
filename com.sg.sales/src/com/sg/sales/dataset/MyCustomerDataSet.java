package com.sg.sales.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.TeamControled;
import com.sg.widgets.part.CurrentAccountContext;

public class MyCustomerDataSet extends SingleDBCollectionDataSetFactory {

	public MyCustomerDataSet() {
		super(IModelConstants.DB, Sales.C_COMPANY);
		String userid = new CurrentAccountContext().getConsignerId();
		setQueryCondition(TeamControled.getVisitableCondition(userid));
	}

}
