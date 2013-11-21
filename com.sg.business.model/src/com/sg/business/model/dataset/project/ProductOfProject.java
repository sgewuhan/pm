package com.sg.business.model.dataset.project;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProductItem;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProductOfProject extends MasterDetailDataSetFactory {

	public ProductOfProject() {
		super(IModelConstants.DB, IModelConstants.C_PRODUCT);
	}

	@Override
	protected String getDetailCollectionKey() {
		return ProductItem.F_PROJECT_ID;
	}

}
