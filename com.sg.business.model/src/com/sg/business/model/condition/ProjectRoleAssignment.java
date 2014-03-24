package com.sg.business.model.condition;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IRelationConditionProvider;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IRoleParameter;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.Role;

public class ProjectRoleAssignment implements IRelationConditionProvider {

	@Override
	public DBObject getCondition(PrimaryObject primaryObject) {
		ProjectRole projectRole = (ProjectRole) primaryObject;
		ObjectId roleId = (ObjectId) projectRole.getValue("role_id");
		if (roleId != null) {
			Project project = projectRole.getProject();
			Role role = ModelService.createModelObject(Role.class, roleId);
			IRoleParameter roleParameter = project.getAdapter(IRoleParameter.class);
			List<PrimaryObject> ass = role.getAssignment(roleParameter);
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
