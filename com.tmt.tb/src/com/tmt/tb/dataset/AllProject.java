package com.tmt.tb.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;

public class AllProject extends SingleDBCollectionDataSetFactory {

	public AllProject() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
	}

}
