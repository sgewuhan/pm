package com.sg.sales.model.dataset;

import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.ICompanyRelative;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class CompanyOpportunityDataSet extends MasterDetailDataSetFactory {

	public CompanyOpportunityDataSet() {
		super(IModelConstants.DB, Sales.C_OPPORTUNITY);
	}

	@Override
	protected String getDetailCollectionKey() {
		return ICompanyRelative.F_COMPANY_ID;
	}

}
