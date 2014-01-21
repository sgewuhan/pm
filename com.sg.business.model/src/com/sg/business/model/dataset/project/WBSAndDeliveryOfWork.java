package com.sg.business.model.dataset.project;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class WBSAndDeliveryOfWork extends MasterDetailDataSetFactory{

	public WBSAndDeliveryOfWork() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
	}

	@Override
	protected String getDetailCollectionKey() {
		return Work.F_PARENT_ID;
	}


}
