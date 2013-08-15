package com.sg.business.model.dataset.organization;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;

public class OrgRoot extends SingleDBCollectionDataSetFactory {

	public OrgRoot() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
		setQueryCondition(new BasicDBObject().append(Organization.F_PARENT_ID, null));
	}


	
}
