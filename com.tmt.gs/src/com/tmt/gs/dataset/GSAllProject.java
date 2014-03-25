package com.tmt.gs.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;

public class GSAllProject extends SingleDBCollectionDataSetFactory {

	public GSAllProject() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
	}

}
