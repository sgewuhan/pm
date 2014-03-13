package com.sg.sales.model.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;

public class AllContractDataSet extends SingleDBCollectionDataSetFactory {

	public AllContractDataSet() {
		super(IModelConstants.DB, Sales.C_CONTRACT);
	}

}
