package com.sg.business.commons.engineeringchange;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;

public class ProjectFunctionOrg extends SingleDBCollectionDataSetFactory {

	public ProjectFunctionOrg() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
		setQueryCondition(new BasicDBObject().append(Organization.F_IS_FUNCTION_DEPARTMENT, Boolean.TRUE));
	}
}
