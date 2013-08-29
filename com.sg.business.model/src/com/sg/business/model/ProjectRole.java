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

/**
 * ��Ŀ��ɫ
 * @author jinxitao
 *
 */
public class ProjectRole extends AbstractRoleDefinition implements
		IProjectRelative {
	/**
	 * ������ɫ�ı༭��
	 */
	public static final String EDITOR_ROLE_DEFINITION_CREATE = "editor.projectrole";

	/**
	 * �༭��Ŀ��ɫ�༭��
	 */
	public static final String EDITOR_ROLE_DEFINITION_EDIT = "editor.projectrole.edit";

	/**
	 * ���ؽ�ɫ������Ŀ
	 * @return Project
	 */
	public Project getProject() {
		ObjectId projectId = getProjectId();
		if (projectId != null) {
			return ModelService.createModelObject(Project.class, projectId);
		}
		return null;
	}
	
	/**
	 * ���ؽ�ɫ������Ŀ_id
	 * @return ObjectId
	 */
	public ObjectId getProjectId() {
		return (ObjectId) getValue(F_PROJECT_ID);
	}

	/**
	 * ����һ����Ŀ��ɫ��¼�����ݿ���
	 */
	@Override
	public void doInsert(IContext context) throws Exception {
		//�����ϵͳ��ɫ����Ҫ���ý�ɫ���û���ӵ���Ŀ�Ĳ�����
		if(isOrganizatioRole()){
			Role role = getOrganizationRole();
			List<PrimaryObject> ass = role.getAssignment();
			Project project = getProject();
			project.doAddParticipateFromAssignment(ass);
		}
		
		super.doInsert(context);
	}


	/**
	 * Ϊ��ɫָ���û�
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

		// д�뵽��Ŀ
		if(userIds.length>0){
			Project project = getProject();
			project.doAddParticipate(userIds);
		}

		// д��־
		DBUtil.SAVELOG(context.getAccountInfo().getUserId(), "Ϊ��ɫָ���û�",
				new Date(), "��ɫ��" + this + "\n�û�" + users.toString(),
				IModelConstants.DB);

	}


	/**
	 * ɾ����Ŀ��ɫ
	 */
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

	/**
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "��Ŀ��ɫ";
	}

	/**
	 * ���ؽ�ɫָ��
	 * @return List
	 */
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
