package com.sg.sales.dataset;

import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

public class CompanyDataSet extends OptionDataSetFactory {

	public CompanyDataSet() {
		super(IModelConstants.DB, Sales.C_COMPANY);
	}



}
