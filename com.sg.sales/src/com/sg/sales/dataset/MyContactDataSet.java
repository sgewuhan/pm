package com.sg.sales.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;

public class MyContactDataSet extends SingleDBCollectionDataSetFactory {

	public MyContactDataSet() {
		super(IModelConstants.DB, Sales.C_CONTACT);
	}

}
