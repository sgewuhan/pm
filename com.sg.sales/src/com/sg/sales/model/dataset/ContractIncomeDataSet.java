package com.sg.sales.model.dataset;

import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.Income;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ContractIncomeDataSet extends  MasterDetailDataSetFactory {

	public ContractIncomeDataSet() {
		super(IModelConstants.DB, Sales.C_INCOME);
	}

	@Override
	protected String getDetailCollectionKey() {
		return Income.F_CONTRACT_ID;
	}

}
