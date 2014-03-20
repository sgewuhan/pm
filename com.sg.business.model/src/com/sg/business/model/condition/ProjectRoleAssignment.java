package com.sg.business.model.condition;

import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IRelationConditionProvider;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.Role;
import com.sg.business.model.RoleParameter;
import com.sg.business.model.User;

public class ProjectRoleAssignment implements IRelationConditionProvider {

	@Override
	public DBObject getCondition(PrimaryObject primaryObject) {
		ProjectRole projectRole = (ProjectRole) primaryObject;
		ObjectId roleId = (ObjectId) projectRole.getValue("role_id");
		if (roleId != null) {
			Project project = projectRole.getProject();
			Role role = ModelService.createModelObject(Role.class, roleId);
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(RoleParameter.TYPE, RoleParameter.TYPE_PROJECT);
			parameters.put(RoleParameter.PROJECT_ID, project.get_id());
			parameters.put(RoleParameter.PROJECT, project);
			parameters.put(RoleParameter.PROJECT_BUSINESS_ORGANIZATION,
					project.getBusinessOrganization());

			User charger = project.getCharger();
			if (charger != null) {
				parameters.put(RoleParameter.PROJECT_CHARGER,
						charger.getUserid());
			} else {
				parameters.put(RoleParameter.PROJECT_CHARGER, "");
			}
			parameters.put(RoleParameter.PROJECT_FUNCTION_ORGANIZATION,
					project.getFunctionOrganization());
			parameters.put(RoleParameter.PROJECT_LAUNCH_ORGANIZATION,
					project.getLaunchOrganization());
			parameters.put(RoleParameter.PROJECT_PRODUCT_OPTION,
					project.getProductTypeOptions());
			parameters.put(RoleParameter.PROJECT_STANDARD_OPTION,
					project.getStandardSetOptions());
			parameters.put(RoleParameter.PROJECT_TYPE_OPTION,
					project.getProjectTypeOptions());
			List<PrimaryObject> ass = role.getAssignment(parameters);
			ObjectId[] ids = new ObjectId[ass.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = ass.get(i).get_id();
			}
			return new BasicDBObject().append(PrimaryObject.F__ID,
					new BasicDBObject().append("$in", ids));
		}
		return new BasicDBObject().append(PrimaryObject.F__ID, null);
	}

}
