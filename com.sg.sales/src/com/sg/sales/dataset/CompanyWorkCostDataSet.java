package com.sg.sales.dataset;

import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.ICompanyRelatied;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class CompanyWorkCostDataSet extends MasterDetailDataSetFactory {

	public CompanyWorkCostDataSet() {
		super(IModelConstants.DB, Sales.C_WORKCOST);
	}

	@Override
	protected String getDetailCollectionKey() {
		return ICompanyRelatied.F_COMPANY_ID;
	}

}
