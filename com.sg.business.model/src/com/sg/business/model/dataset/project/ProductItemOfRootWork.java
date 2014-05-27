package com.sg.business.model.dataset.project;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProductItem;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProductItemOfRootWork extends MasterDetailDataSetFactory {

	public ProductItemOfRootWork() {
		super(IModelConstants.DB, IModelConstants.C_PRODUCT);
	}

	@Override
	protected String getDetailCollectionKey() {
		return ProductItem.F_PROJECT_ID;
	}

	@Override
	protected Object getMasterValue() {
		if (master instanceof Work) {
			Project project = ((Work) master).getProject();
			if (project != null) {
				return project.get_id();
			}
		}
		return super.getMasterValue();
	}
}
