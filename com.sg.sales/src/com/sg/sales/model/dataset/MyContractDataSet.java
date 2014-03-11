package com.sg.sales.model.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.TeamControl;
import com.sg.widgets.part.CurrentAccountContext;

public class MyContractDataSet extends SingleDBCollectionDataSetFactory {

	public MyContractDataSet() {
		super(IModelConstants.DB, Sales.C_CONTRACT);
		String userid = new CurrentAccountContext().getConsignerId();
		setQueryCondition(TeamControl.getVisitableCondition(userid));
	}

}
