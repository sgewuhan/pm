package com.sg.sales.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;

public class MyOpportunityDataSet extends SingleDBCollectionDataSetFactory {

	public MyOpportunityDataSet() {
		super(IModelConstants.DB, Sales.C_OPPORTUNITY);
	}

}
