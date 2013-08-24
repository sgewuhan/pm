package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.utils.DBUtil;
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
		ObjectId projectId = getProjectId();
		if (projectId != null) {
			return ModelService.createModelObject(Project.class, projectId);
		}
		return null;
	}
	
	
	public ObjectId getProjectId() {
		return (ObjectId) getValue(F_PROJECT_ID);
	}

	@Override
	public void doInsert(IContext context) throws Exception {
		//如果是系统角色，需要将该角色的用户添加到项目的参与者
		if(isOrganizatioRole()){
			Role role = getOrganizationRole();
			List<PrimaryObject> ass = role.getAssignment();
			String[] userIds = new String[ass.size()];
			for (int i = 0; i < ass.size(); i++) {
				 RoleAssignment ra = (RoleAssignment) ass.get(i);
				 userIds[i] = ra.getUserid();
			}
			if(userIds.length>0){
				Project project = getProject();
				project.doAddProjectParticipate(userIds);
			}
		}
		
		super.doInsert(context);
	}


	/**
	 * 为角色指派用户
	 * 
	 * @param users
	 * @throws Exception
	 */
	public void doAssignUsers(List<?> users, IContext context) throws Exception {
		DBCollection roleAssignmentCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
		List<DBObject> list = new ArrayList<DBObject>();
		String[] userIds = new String[users.size()];
		for (int i = 0; i < users.size(); i++) {
			User user = (User) users.get(i);
			UserSessionContext.noticeAccountChanged(user.getUserid(),
					UserSessionContext.EVENT_ROLE_CHANGED);
			list.add(new BasicDBObject()
					.append(ProjectRoleAssignment.F__TYPE,
							IModelConstants.C_ROLE_ASSIGNMENT)
					.append(ProjectRoleAssignment.F_USER_ID, user.getUserid())
					.append(ProjectRoleAssignment.F_USER_NAME,
							user.getUsername())
					.append(ProjectRoleAssignment.F_ROLE_ID, get_id())
					.append(ProjectRoleAssignment.F_ROLE_NUMBER,
							getRoleNumber())
					.append(ProjectRoleAssignment.F_ROLE_NAME, getDesc()));

			userIds[i] = user.getUserid();
		}
		WriteResult ws = roleAssignmentCol.insert(list);
		checkWriteResult(ws);

		// 写入到项目
		if(userIds.length>0){
			Project project = getProject();
			project.doAddProjectParticipate(userIds);
		}

		// 写日志
		DBUtil.SAVELOG(context.getAccountInfo().getUserId(), "为角色指派用户",
				new Date(), "角色：" + this + "\n用户" + users.toString(),
				IModelConstants.DB);

	}


	@Override
	public void doRemove(IContext context) throws Exception {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
		WriteResult ws = col
				.remove(new BasicDBObject().append(
						ProjectRoleAssignment.F_ROLE_ID, get_id()),
						WriteConcern.NORMAL);
		checkWriteResult(ws);
		super.doRemove(context);
	}

	@Override
	public String getTypeName() {
		return "项目角色";
	}

	public List<PrimaryObject> getAssignment() {
		if (isOrganizatioRole()) {
			Role role = getOrganizationRole();
			return role.getAssignment();
		} else {
			return getRelationById(F__ID, ProjectRoleAssignment.F_ROLE_ID,
					ProjectRoleAssignment.class);
		}
	}
}
