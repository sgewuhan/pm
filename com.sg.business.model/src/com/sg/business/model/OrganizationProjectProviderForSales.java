package com.sg.business.model;

public class OrganizationProjectProviderForSales extends
		OrganizationProjectProvider {

	@Override
	protected String getOrganizationFieldName() {
		return Project.F_BUSINESS_ORGANIZATION;
	}

	@Override
	protected String getRelatedUserFieldName() {
		return Project.F_BUSINESS_CHARGER;
	}
}
