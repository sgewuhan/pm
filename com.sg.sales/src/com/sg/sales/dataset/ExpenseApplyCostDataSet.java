package com.sg.sales.dataset;

import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.WorkCost;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ExpenseApplyCostDataSet extends MasterDetailDataSetFactory{

	public ExpenseApplyCostDataSet() {
		super(IModelConstants.DB, Sales.C_WORKCOST);
		
	}

	@Override
	protected String getDetailCollectionKey() {
		return WorkCost.F_APPLY_WORK_ID;
	}
}
