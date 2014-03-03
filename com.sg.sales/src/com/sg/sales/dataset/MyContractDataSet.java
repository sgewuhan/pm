package com.sg.sales.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;

public class MyContractDataSet extends SingleDBCollectionDataSetFactory {

	public MyContractDataSet() {
		super(IModelConstants.DB, Sales.C_CONTRACT);
	}

}
