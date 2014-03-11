package com.sg.sales.model.dataset;

import com.sg.business.model.IModelConstants;
import com.sg.sales.model.ISalesWork;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class CompanyWorkDataSet extends MasterDetailDataSetFactory {

	public CompanyWorkDataSet() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
	}

	@Override
	protected String getDetailCollectionKey() {
		return ISalesWork.F_COMPANY_ID;
	}

}
