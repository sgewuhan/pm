package com.sg.business.visualization.data;

import com.sg.business.model.Project;

public class ProjectSetContentProviderForSales extends
		ProjectSetContentProvider {

	@Override
	protected String getOrganizationFieldName() {
		return Project.F_BUSINESS_ORGANIZATION;
	}

	@Override
	protected String getRelationName() {
		return "organization_businessmanager";
	}

	@Override
	protected String getRelatedUserFieldName() {
		return Project.F_BUSINESS_CHARGER;
	}

}
