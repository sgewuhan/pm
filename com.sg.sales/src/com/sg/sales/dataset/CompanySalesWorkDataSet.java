package com.sg.sales.dataset;

import com.sg.business.model.IModelConstants;
import com.sg.sales.model.ISalesWork;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class CompanySalesWorkDataSet extends MasterDetailDataSetFactory {

	public CompanySalesWorkDataSet() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
	}

	@Override
	protected String getDetailCollectionKey() {
		return ISalesWork.F_COMPANY_ID;
	}

}
