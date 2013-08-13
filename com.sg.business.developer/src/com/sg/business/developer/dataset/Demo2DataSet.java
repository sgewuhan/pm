package com.sg.business.developer.dataset;

import com.sg.business.developer.model.Demo2;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class Demo2DataSet extends MasterDetailDataSetFactory {

	public Demo2DataSet() {
		super(IModelConstants.DB, "demo2");
	}

	@Override
	protected String getDetailCollectionKey() {
		return Demo2.F_DEMO1_ID;
	}

}
