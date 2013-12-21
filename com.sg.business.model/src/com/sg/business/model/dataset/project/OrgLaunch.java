package com.sg.business.model.dataset.project;

import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class OrgLaunch extends MasterDetailDataSetFactory {

	public OrgLaunch() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	@Override
	protected String getDetailCollectionKey() {
		return Organization.F__ID;
	}

	@Override
	protected Object getMasterValue() {
		Object ids = master.getValue(Project.F_LAUNCH_ORGANIZATION);
		if(ids == null){
			return null;
		}
		return new BasicDBObject().append("$in", ids); //$NON-NLS-1$
	}
}
