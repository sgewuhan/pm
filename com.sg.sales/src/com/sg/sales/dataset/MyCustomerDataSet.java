package com.sg.sales.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;

public class MyCustomerDataSet extends SingleDBCollectionDataSetFactory {

	public MyCustomerDataSet() {
		super(IModelConstants.DB, Sales.C_COMPANY);
	}

}
