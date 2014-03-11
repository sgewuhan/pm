package com.sg.sales.model.dataset;

import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.ICompanyRelative;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class CompanyContractDataSet extends MasterDetailDataSetFactory {

	public CompanyContractDataSet() {
		super(IModelConstants.DB, Sales.C_CONTRACT);
	}

	@Override
	protected String getDetailCollectionKey() {
		return ICompanyRelative.F_COMPANY_ID;
	}

}
