package com.sg.sales.model.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.TeamControl;
import com.sg.widgets.part.CurrentAccountContext;

public class MyCustomerDataSet extends SingleDBCollectionDataSetFactory {

	public MyCustomerDataSet() {
		super(IModelConstants.DB, Sales.C_COMPANY);
		String userid = new CurrentAccountContext().getConsignerId();
		setQueryCondition(TeamControl.getVisitableCondition(userid));
	}

}
