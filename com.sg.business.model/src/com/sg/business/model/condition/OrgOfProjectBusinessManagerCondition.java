package com.sg.business.model.condition;

import com.sg.business.model.Project;

public class OrgOfProjectBusinessManagerCondition extends
		OrgOfProjectManagerCondition {

	@Override
	protected String getOrgFieldName() {
		return Project.F_BUSINESS_ORGANIZATION;
	}
}
