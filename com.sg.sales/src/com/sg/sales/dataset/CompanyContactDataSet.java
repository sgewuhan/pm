package com.sg.sales.dataset;

import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.ICompanyRelatied;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class CompanyContactDataSet extends MasterDetailDataSetFactory {

	public CompanyContactDataSet() {
		super(IModelConstants.DB, Sales.C_CONTACT);
	}

	@Override
	protected String getDetailCollectionKey() {
		return ICompanyRelatied.F_COMPANY_ID;
	}

}
