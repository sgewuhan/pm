package com.tmt.gs.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;

public class AllProjectOfGS extends SingleDBCollectionDataSetFactory {

	public AllProjectOfGS() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
	}

}
