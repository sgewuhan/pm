package com.sg.business.model.dataset.project;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;

public class AllProjects extends SingleDBCollectionDataSetFactory {

	public AllProjects() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
		setSort(new BasicDBObject().append(PrimaryObject.F__ID, 1));
	}
}