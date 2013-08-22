package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

public class ProjectRole extends AbstractRoleDefinition implements
		IProjectRelative {
	/**
	 * 创建角色的编辑器
	 */
	public static final String EDITOR_ROLE_DEFINITION_CREATE = "editor.projectrole";

	public static final String EDITOR_ROLE_DEFINITION_EDIT = "editor.projectrole.edit";

	public Project getProject() {
		ObjectId projectId = (ObjectId) getValue(F_PROJECT_ID);
		if (projectId != null) {
			return ModelService.createModelObject(Project.class, projectId);
		}
		return null;
	}

	/**
	 * 为角色指派用户
	 * @param user
	 * @throws Exception 
	 */
	public void doAssignUsers(List<?> users) throws Exception {
		DBCollection roleAssignmentCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
		List<DBObject> list = new ArrayList<DBObject>();

		for (int i = 0; i < users.size(); i++) {
			User user = (User) users.get(i);
			UserSessionContext.noticeAccountChanged(user.getUserid(),
					UserSessionContext.EVENT_ROLE_CHANGED);
			list.add(new BasicDBObject()
					.append(ProjectRoleAssignment.F__TYPE,IModelConstants.C_ROLE_ASSIGNMENT)
					.append(ProjectRoleAssignment.F_USER_ID, user.getUserid())
					.append(ProjectRoleAssignment.F_USER_NAME, user.getUsername())
					.append(ProjectRoleAssignment.F_ROLE_ID, get_id())
					.append(ProjectRoleAssignment.F_ROLE_NUMBER, getRoleNumber())
					.append(ProjectRoleAssignment.F_ROLE_NAME, getDesc()));
		}
		WriteResult ws = roleAssignmentCol.insert(list);
		checkWriteResult(ws);
	}
	
	@Override
	public void doRemove(IContext context) throws Exception {
		DBCollection col = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
		WriteResult ws = col.remove(new BasicDBObject().append(ProjectRoleAssignment.F_ROLE_ID, get_id()),WriteConcern.NORMAL);
		checkWriteResult(ws);
		super.doRemove(context);
	}
	
	@Override
	public String getTypeName() {
		return "项目角色";
	}
}
