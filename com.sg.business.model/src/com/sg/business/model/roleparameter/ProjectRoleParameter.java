package com.sg.business.model.roleparameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sg.business.model.IRoleParameter;
import com.sg.business.model.Project;
import com.sg.business.model.User;

public class ProjectRoleParameter implements IRoleParameter {

	private Project project;

	public ProjectRoleParameter(Project project) {
		this.project = project;
	}

	@Override
	public Map<String, Object> getParameters() {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IRoleParameter.TYPE, IRoleParameter.TYPE_PROJECT);
		parameters.put(IRoleParameter.PROJECT_ID, project.get_id());
		parameters.put(IRoleParameter.PROJECT, this);
		parameters.put(IRoleParameter.PROJECT_BUSINESS_ORGANIZATION,
				project.getBusinessOrganization());
		User charger = project.getCharger();
		if (charger != null) {
			parameters.put(IRoleParameter.PROJECT_CHARGER, charger.getUserid());
		} else {
			parameters.put(IRoleParameter.PROJECT_CHARGER, "");
		}
		parameters.put(IRoleParameter.PROJECT_FUNCTION_ORGANIZATION,
				project.getFunctionOrganization());
		parameters.put(IRoleParameter.PROJECT_LAUNCH_ORGANIZATION,
				project.getLaunchOrganization());
		List<String> productTypeOptions = project.getProductTypeOptions();
		if (productTypeOptions != null) {
			parameters.put(IRoleParameter.PROJECT_PRODUCT_OPTION,
					productTypeOptions);
		} else {
			parameters.put(IRoleParameter.PROJECT_PRODUCT_OPTION, "");
		}
		List<String> standardSetOptions = project.getStandardSetOptions();
		if (standardSetOptions != null) {
			parameters.put(IRoleParameter.PROJECT_STANDARD_OPTION,
					standardSetOptions);
		} else {
			parameters.put(IRoleParameter.PROJECT_STANDARD_OPTION, "");
		}
		List<String> projectTypeOptions = project.getProjectTypeOptions();
		if (projectTypeOptions != null) {
			parameters.put(IRoleParameter.PROJECT_TYPE_OPTION,
					projectTypeOptions);
		} else {
			parameters.put(IRoleParameter.PROJECT_TYPE_OPTION, "");
		}
		return parameters;
	}

}
