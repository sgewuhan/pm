package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.eclipse.swt.SWT;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.utils.DBUtil;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.sg.business.model.event.AccountEvent;
import com.sg.business.model.toolkit.ProjectToolkit;
import com.sg.business.resource.nls.Messages;

/**
 * ��Ŀ��ɫ
 * 
 * @author jinxitao
 * 
 */
public class ProjectRole extends AbstractRoleDefinition implements
		IProjectRelative {

	/**
	 * ������ɫ�ı༭��
	 */
	public static final String EDITOR_ROLE_DEFINITION_CREATE = "editor.projectrole"; //$NON-NLS-1$

	/**
	 * �༭��Ŀ��ɫ�༭��
	 */
	public static final String EDITOR_ROLE_DEFINITION_EDIT = "editor.projectrole.edit"; //$NON-NLS-1$

	/**
	 * ���ؽ�ɫ������Ŀ
	 * 
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
	 * 
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
		// �����ϵͳ��ɫ����Ҫ���ý�ɫ���û���ӵ���Ŀ�Ĳ�����
		if (isOrganizatioRole()) {
			Role role = getOrganizationRole();
			List<PrimaryObject> ass = role.getAssignment();
			Project project = getProject();
			project.doAddParticipateFromAssignment(ass);
		}

		super.doInsert(context);
	}

	/**
	 * Ϊ��ɫָ���û�
	 * 
	 * @param users
	 * @throws Exception
	 */
	public void doAssignUsers(List<User> users, IContext context)
			throws Exception {
		DBCollection roleAssignmentCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
		List<DBObject> list = new ArrayList<DBObject>();
		String[] userIds = new String[users.size()];
		for (int i = 0; i < users.size(); i++) {
			User user = (User) users.get(i);
			list.add(new BasicDBObject()
					.append(ProjectRoleAssignment.F__TYPE,
							IModelConstants.C_ROLE_ASSIGNMENT)
					.append(ProjectRoleAssignment.F_USER_ID, user.getUserid())
					.append(ProjectRoleAssignment.F_USER_NAME,
							user.getUsername())
					.append(ProjectRoleAssignment.F_ROLE_ID, get_id())
					.append(ProjectRoleAssignment.F_ROLE_NUMBER,
							getRoleNumber())
					.append(ProjectRoleAssignment.F_ROLE_NAME, getDesc())
					.append(ProjectRoleAssignment.F_PROJECT_ID, getProjectId()));

			userIds[i] = user.getUserid();
		}

		// ������ظ����������µĴ������׳�MongoException Code = 11000
		WriteResult ws = roleAssignmentCol.insert(list);
		checkWriteResult(ws);

		// д�뵽��Ŀ
		if (userIds.length > 0) {
			Project project = getProject();
			project.doAddParticipate(userIds);
		}

		// ֪ͨ�ʻ�
		for (int i = 0; i < users.size(); i++) {
			User user = (User) users.get(i);
			UserSessionContext.noticeAccountChanged(user.getUserid(),
					new AccountEvent(AccountEvent.EVENT_ROLE_CHANGED, this));
		}

		// д��־
		DBUtil.SAVELOG(context.getAccountInfo().getUserId(), Messages.get().ProjectRole_2,
				new Date(), Messages.get().ProjectRole_3 + this + Messages.get().ProjectRole_4 + users.toString(),
				IModelConstants.DB);

	}

	/**
	 * ɾ����ɫ���
	 */
	public List<Object[]> checkRemoveAction(IContext context) {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.WBS���õĽ�ɫ
		BasicDBList values = new BasicDBList();
		values.add(new BasicDBObject().append(
				Work.F_ASSIGNMENT_CHARGER_ROLE_ID, getOrganizationRoleId()));
		values.add(new BasicDBObject().append(Work.F_CHARGER_ROLE_ID,
				getOrganizationRoleId()));
		values.add(new BasicDBObject().append(Work.F_PARTICIPATE_ROLE_SET,
				Pattern.compile("^.*" + getOrganizationRoleId() + ".*$", //$NON-NLS-1$ //$NON-NLS-2$
						Pattern.CASE_INSENSITIVE)));
		long countWork = getRelationCountByCondition(
				Work.class,
				new BasicDBObject().append("$or", values).append( //$NON-NLS-1$
						Work.F_PARENT_ID, getProjectId()));
		if (countWork > 0) {
			message.add(new Object[] { Messages.get().ProjectRole_8, this, SWT.ICON_WARNING });
		}

		Project project = getProject();
		IProcessControl pc = (IProcessControl) project
				.getAdapter(IProcessControl.class);
		// 2.��Ŀִ������������
		if (ProjectToolkit.checkProcessInternal(pc, Project.F_WF_COMMIT, this)) {
			message.add(new Object[] { Messages.get().ProjectRole_9, this,
					SWT.ICON_WARNING });
		}

		// 3.��Ŀ�������������
		if (ProjectToolkit.checkProcessInternal(pc, Project.F_WF_CHANGE, this)) {
			message.add(new Object[] { Messages.get().ProjectRole_10, this,
					SWT.ICON_WARNING });
		}
		// 4.��������������
		Work root = project.getWBSRoot();
		message.addAll(checkCascadeRemove(root));
		return message;
	}

	private List<Object[]> checkCascadeRemove(Work work) {
		List<Object[]> message = new ArrayList<Object[]>();
		List<PrimaryObject> childrenWork = work.getChildrenWork();
		if (childrenWork.size() > 0) {// ������¼��������¼��ļ����
			for (int i = 0; i < childrenWork.size(); i++) {
				Work childWork = (Work) childrenWork.get(i);
				message.addAll(checkCascadeRemove(childWork));
			}
		} else {
			IProcessControl pc = (IProcessControl) work
					.getAdapter(IProcessControl.class);
			// 4.1.����ִ������������
			if (ProjectToolkit
					.checkProcessInternal(pc, Work.F_WF_EXECUTE, this)) {
				message.add(new Object[] { Messages.get().ProjectRole_11, work,
						SWT.ICON_WARNING });
			}

			// 4.2.�����������������
			if (ProjectToolkit.checkProcessInternal(pc, Work.F_WF_CHANGE, this)) {
				message.add(new Object[] { Messages.get().ProjectRole_12, work,
						SWT.ICON_WARNING });
			}
		}
		return message;
	}

	/**
	 * ɾ����Ŀ��ɫ
	 * 
	 * 1.���WBS�ϵĽ�ɫ 2.���������õĽ�ɫ
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
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().ProjectRole_13;
	}

	/**
	 * ���ؽ�ɫָ��
	 * 
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

	public boolean hasOtherAssignment(String otherUserId) {
		long count = getRelationCountByCondition(
				ProjectRole.class,
				new BasicDBObject().append(ProjectRoleAssignment.F_ROLE_ID,
						get_id()).append(ProjectRoleAssignment.F_USER_ID,
						otherUserId));
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
}
