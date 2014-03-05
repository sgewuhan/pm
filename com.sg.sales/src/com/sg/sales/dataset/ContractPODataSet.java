package com.sg.sales.dataset;

import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.POItem;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ContractPODataSet extends MasterDetailDataSetFactory {

	public ContractPODataSet() {
		super(IModelConstants.DB, Sales.C_POITEM);
	}

	@Override
	protected String getDetailCollectionKey() {
		return POItem.F_CONTRACT_ID;
	}

}
