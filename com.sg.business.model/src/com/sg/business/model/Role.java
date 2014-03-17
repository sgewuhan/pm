package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.javascript.JavaScriptEvaluator;
import com.mobnut.commons.util.Utils;
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
import com.sg.business.model.event.AccountEvent;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;

/**
 * ��ɫ
 * <p/>
 * ��֯�еĽ�ɫ
 * 
 * @author jinxitao
 * 
 */
public class Role extends PrimaryObject {

	/**
	 * ������֯ID
	 */
	public static final String F_ORGANIZATION_ID = "organization_id"; //$NON-NLS-1$

	/**
	 * ��ɫ���
	 */
	public static final String F_ROLE_NUMBER = "rolenumber"; //$NON-NLS-1$

	/**
	 * Ĭ�ϱ༭��
	 */
	public static final String EDITOR_DEFAULT = "editor.organization.role"; //$NON-NLS-1$

	/**
	 * ������ɫ/��֯��ɫ/ϵͳ����Ա
	 */
	public static final String ROLE_ADMIN_ID = "T000"; //$NON-NLS-1$
	public static final String ROLE_ADMIN_TEXT = Messages.get().Role_4;

	/**
	 * ������ɫ/��֯��ɫ/��֯����Ա
	 */
	public static final String ROLE_ORGANIZATION_ADMIN_ID = "T000"; //$NON-NLS-1$
	public static final String ROLE_ORGANIZATION_ADMIN_TEXT = Messages.get().Role_6;

	/**
	 * ������ɫ/��֯��ɫ/�ĵ�����Ա
	 */
	public static final String ROLE_VAULT_ADMIN_ID = "T001"; //$NON-NLS-1$
	public static final String ROLE_VALUT_ADMIN_TEXT = Messages.get().Role_8;

	/**
	 * ������ɫ/��֯��ɫ/��Ŀ����Ա
	 */
	public static final String ROLE_PROJECT_ADMIN_ID = "T002"; //$NON-NLS-1$
	public static final String ROLE_PROJECT_ADMIN_TEXT = Messages.get().Role_10;

	/**
	 * ������ɫ/��֯��ɫ/ҵ�����Ա
	 */
	public static final String ROLE_BUSINESS_ADMIN_ID = "T004"; //$NON-NLS-1$
	public static final String ROLE_BUSINESS_ADMIN_TEXT = Messages.get().Role_12;

	/**
	 * ������ɫ/��֯��ɫ/�ĵ�������
	 */
	public static final String ROLE_VAULT_GUEST_ID = "T003"; //$NON-NLS-1$
	public static final String ROLE_VAULT_GUEST_TEXT = Messages.get().Role_14;

	/**
	 * ������ɫ/��֯��ɫ/���ž���
	 */
	public static final String ROLE_DEPT_MANAGER_ID = "T005"; //$NON-NLS-1$
	public static final String ROLE_DEPT_MANAGER_TEXT = Messages.get().Role_16;

	/**
	 * ������ɫ/��֯��ɫ/�������ý�ɫֻ�ܳ�������ҵ���������֯
	 */
	public static final String ROLE_FINANCIAL_MANAGER_ID = "T006"; //$NON-NLS-1$
	public static final String ROLE_FINANCIAL_MANAGER_TEXT = Messages.get().Role_18;

	/**
	 * ϵͳ��ɫID
	 */
	public static final String[] ROLE_ID_SYSTEM = new String[] { ROLE_ADMIN_ID,
			ROLE_ORGANIZATION_ADMIN_ID, ROLE_PROJECT_ADMIN_ID,
			ROLE_BUSINESS_ADMIN_ID, ROLE_VAULT_ADMIN_ID, ROLE_VAULT_GUEST_ID,
			ROLE_DEPT_MANAGER_ID, ROLE_FINANCIAL_MANAGER_ID };

	/**
	 * ϵͳ��ɫ����
	 */
	public static final String[] ROLE_NAME_SYSTEM = new String[] {
			ROLE_ADMIN_TEXT, ROLE_ORGANIZATION_ADMIN_TEXT,
			ROLE_BUSINESS_ADMIN_TEXT, ROLE_PROJECT_ADMIN_TEXT,
			ROLE_VALUT_ADMIN_TEXT, ROLE_VAULT_GUEST_TEXT,
			ROLE_DEPT_MANAGER_TEXT, ROLE_FINANCIAL_MANAGER_TEXT };

	public static final String ROLE_ASSIGNMENT_ID = "T007"; //$NON-NLS-1$
	public static final String ROLE_ASSIGNMENT_TEXT = Messages.get().Role_20;

	public static final String F_RULE = "rule";

	/**
	 * ��ɫ��ϵͳ�еĵ���ʾ����
	 */
	@Override
	public String getLabel() {
		return getDesc() + "|" + getRoleNumber(); //$NON-NLS-1$
	}

	/**
	 * ��ɫ��ϵͳ�е���ʾͼ��
	 */
	@Override
	public Image getImage() {
		if (isSystemRole()) {
			return BusinessResource.getImage(BusinessResource.IMAGE_ROLE2_16);
		} else {
			return BusinessResource.getImage(BusinessResource.IMAGE_ROLE_16);
		}
	}

	/**
	 * ��ȡ��ɫ������֯��_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getOrganization_id() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	/**
	 * ����û�����ɫ��
	 * 
	 * @param users
	 *            ���û�����
	 * @throws Exception
	 */
	public void doAssignUsers(List<PrimaryObject> users, IContext context)
			throws Exception {
		DBCollection roleAssignmentCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ROLE_ASSIGNMENT);
		List<DBObject> list = new ArrayList<DBObject>();

		for (int i = 0; i < users.size(); i++) {
			User user = (User) users.get(i);
			if (!user.isActivated()) {
				continue;
			}
			list.add(new BasicDBObject()
					.append(RoleAssignment.F__TYPE,
							IModelConstants.C_ROLE_ASSIGNMENT)
					.append(RoleAssignment.F_USER_ID, user.getUserid())
					.append(RoleAssignment.F_USER_NAME, user.getUsername())
					.append(RoleAssignment.F_ROLE_ID, get_id())
					.append(RoleAssignment.F_ROLE_NUMBER, getRoleNumber())
					.append(RoleAssignment.F_ROLE_NAME, getDesc()));
		}
		roleAssignmentCol.insert(list);

		// ֪ͨ�ʻ�
		for (int i = 0; i < users.size(); i++) {
			User user = (User) users.get(i);
			UserSessionContext.noticeAccountChanged(user.getUserid(),
					new AccountEvent(AccountEvent.EVENT_ROLE_CHANGED, this));
		}

		DBUtil.SAVELOG(context.getAccountInfo().getUserId(),
				Messages.get().Role_22, new Date(), Messages.get().Role_23
						+ this + Messages.get().Role_24 + users.toString(),
				IModelConstants.DB);
	}

	public List<PrimaryObject> getAssignment() {
		return getRelationById(F__ID, RoleAssignment.F_ROLE_ID,
				RoleAssignment.class);
	}

	public List<PrimaryObject> getAssignment(Map<String, Object> parameters) {
		String js = getStringValue(F_RULE);
		List<PrimaryObject> rs = getRelationById(F__ID,
				RoleAssignment.F_ROLE_ID, RoleAssignment.class);
		parameters.put(RoleParameter.ASSIGNMENT, rs);
		//ת������
		Object projectId = parameters.get(RoleParameter.PROJECT_ID);
		if(projectId instanceof ObjectId){
			Project project = ModelService.createModelObject(Project.class, (ObjectId)projectId);
			parameters.put(RoleParameter.PROJECT, project);
		}
		if (js != null) {
			Object result = JavaScriptEvaluator.evaluate(js, parameters);
			if (result instanceof String[]) {
				ArrayList<PrimaryObject> rs1 = new ArrayList<PrimaryObject>();
				for (int i = 0; i < rs.size(); i++) {
					Object userid = rs.get(i)
							.getValue(RoleAssignment.F_USER_ID);
					if (Utils.inArray(userid, (String[]) result)) {
						rs1.add(rs.get(i));
					}
				}
				return rs1;
			} else if (result instanceof String) {
				ArrayList<PrimaryObject> rs1 = new ArrayList<PrimaryObject>();
				for (int i = 0; i < rs.size(); i++) {
					Object userid = rs.get(i)
							.getValue(RoleAssignment.F_USER_ID);
					if (Util.equals(userid, result)) {
						rs1.add(rs.get(i));
					}
				}
				return rs1;
			}
		}
		return rs;
	}

	/**
	 * ɾ����ɫ���
	 */
	public List<Object[]> checkRemoveAction(IContext context) {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.��ɫ���µ��û�
		long countUser = getRelationCountByCondition(RoleAssignment.class,
				new BasicDBObject().append(RoleAssignment.F_ROLE_ID, get_id()));
		if (countUser > 0) {
			message.add(new Object[] { Messages.get().Role_25, this,
					SWT.ICON_ERROR });
		}

		// 2.��Ŀģ���RoleDefinition���õĽ�ɫ
		long countRoleDefinition = getRelationCountByCondition(
				RoleAssignment.class, new BasicDBObject().append(
						RoleDefinition.F_ORGANIZATION_ROLE_ID, get_id()));
		if (countRoleDefinition > 0) {
			message.add(new Object[] { Messages.get().Role_26, this,
					SWT.ICON_ERROR });
		}

		// 3.�������������WBS���õĽ�ɫ
		BasicDBList values = new BasicDBList();
		values.add(new BasicDBObject().append(
				WorkDefinition.F_ASSIGNMENT_CHARGER_ROLE_ID, get_id()));
		values.add(new BasicDBObject().append(WorkDefinition.F_CHARGER_ROLE_ID,
				get_id()));
		values.add(new BasicDBObject().append(
				WorkDefinition.F_PARTICIPATE_ROLE_SET, Pattern.compile("^.*" //$NON-NLS-1$
						+ get_id() + ".*$", Pattern.CASE_INSENSITIVE))); //$NON-NLS-1$
		long countWorkDefinition = getRelationCountByCondition(
				WorkDefinition.class,
				new BasicDBObject().append("$or", values).append( //$NON-NLS-1$
						WorkDefinition.F_WORK_TYPE,
						WorkDefinition.WORK_TYPE_STANDLONE));
		if (countWorkDefinition > 0) {
			message.add(new Object[] { Messages.get().Role_30, this,
					SWT.ICON_ERROR });
		}

		// 4.��ĿProjectRole���õĽ�ɫ
		long countProjectRole = getRelationCountByCondition(ProjectRole.class,
				new BasicDBObject().append(ProjectRole.F_ORGANIZATION_ROLE_ID,
						get_id()));
		if (countProjectRole > 0) {
			message.add(new Object[] { Messages.get().Role_31, this,
					SWT.ICON_ERROR });
		}

		// 5.����������WBS���õĽ�ɫ
		values.clear();
		values.add(new BasicDBObject().append(
				Work.F_ASSIGNMENT_CHARGER_ROLE_ID, get_id()));
		values.add(new BasicDBObject().append(Work.F_CHARGER_ROLE_ID, get_id()));
		values.add(new BasicDBObject().append(Work.F_PARTICIPATE_ROLE_SET,
				Pattern.compile("^.*" + get_id() + ".*$", //$NON-NLS-1$ //$NON-NLS-2$
						Pattern.CASE_INSENSITIVE)));
		long countWork = getRelationCountByCondition(Work.class,
				new BasicDBObject().append("$or", values).append( //$NON-NLS-1$
						Work.F_PROJECT_ID, null));
		if (countWork > 0) {
			message.add(new Object[] { Messages.get().Role_35, this,
					SWT.ICON_WARNING });
		}

		return message;
	}

	/**
	 * ɾ����ɫ
	 * 
	 * @param context
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		// ��Ҫ���ǽ�ɫ��Ӧ�õ���Ŀ�����
		// ��ɾ����ɫָ��
		DBCollection raCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE_ASSIGNMENT);
		raCol.remove(new BasicDBObject().append(RoleAssignment.F_ROLE_ID,
				get_id()));
		super.doRemove(context);
	}

	/**
	 * ��ȡ��ɫ���
	 * 
	 * @return String
	 */
	public String getRoleNumber() {
		return (String) getValue(F_ROLE_NUMBER);
	}

	/**
	 * ��ý�ɫ��������֯
	 * 
	 * @return Organization
	 */
	public Organization getOrganization() {
		ObjectId organization_id = getOrganization_id();
		if (organization_id != null) {
			return ModelService.createModelObject(Organization.class,
					organization_id);
		} else {
			return null;
		}
	}

	/**
	 * ��ȡ��ɫ����,��ɫ���ͷ�Ϊϵͳ��ɫ���û��Զ����ɫ
	 * 
	 * @return
	 */
	public String getRoleTypeText() {
		if (isSystemRole()) {
			return Messages.get().Role_36;
		} else {
			return Messages.get().Role_37;
		}
	}

	/**
	 * �жϽ�ɫ�Ƿ���Ը���
	 * 
	 * @param context
	 */
	@Override
	public boolean canEdit(IContext context) {
//		String uid = context.getAccountInfo().getUserId();
//		User user = UserToolkit.getUserById(uid);
//		if (Boolean.TRUE.equals(user.getValue(User.F_IS_ADMIN))) {
//			return true;
//		}
//		// ϵͳ�Ľ�ɫ�����Ը���
//		if (isSystemRole()) {
//			return false;
//		}
		return super.canEdit(context);
	}

	/**
	 * �жϽ�ɫ�Ƿ����ɾ��
	 * 
	 * @param context
	 * @return boolean
	 */
	@Override
	public boolean canDelete(IContext context) {
		if (isSystemRole()) {
			return false;
		}
		return super.canDelete(context);
	}

	/**
	 * �жϽ�ɫ�Ƿ�Ϊϵͳ��ɫ
	 * 
	 * @return boolean
	 */
	public boolean isSystemRole() {
		String rn = getRoleNumber();
		if (Utils.inArray(rn, ROLE_ID_SYSTEM)) {
			// �������֯����������صĽ�ɫ
			if (ROLE_VAULT_ADMIN_ID.equals(rn)
					|| ROLE_VAULT_GUEST_ID.equals(rn)) {
				// �жϸ���֯�Ƿ�������
				Organization org = getOrganization();
				if (Boolean.TRUE.equals(org.isContainer())) {
					return true;
				} else {
					return false;
				}
			} else if (ROLE_PROJECT_ADMIN_ID.equals(rn)
					|| ROLE_BUSINESS_ADMIN_ID.equals(rn)) {
				// �жϸ���֯�Ƿ������Ŀ����ְ��
				Organization org = getOrganization();
				if (Boolean.TRUE.equals(org.isFunctionDepartment())) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * T��ͷ��P��ͷ����ϵͳԤ���Ľ�ɫ��
	 * 
	 * @param rn
	 * @return
	 */
	private boolean isReservedNumber(String rn) {
		return rn != null
				&& (rn.toUpperCase().startsWith("T0") || rn.toUpperCase() //$NON-NLS-1$
						.startsWith("P0")); //$NON-NLS-1$
	}

	/**
	 * �жϽ�ɫ����Ƿ�Ϸ� ��ɫ�Ƿ�Ϊϵͳ������ţ���ɫ������֯�Ƿ���ڣ���ɫ��������֯���Ƿ��Ѿ�����
	 * 
	 * @throws Exception
	 */
	public void check() throws Exception {
		String rn = getRoleNumber();
		// ����ɫ����Ƿ�Ϸ�
		if (isReservedNumber(rn)) {
			throw new Exception(Messages.get().Role_40);
		}
		// ����ɫ����ڵ�ǰ��֯���Ƿ����
		Organization org = getOrganization();
		if (org == null) {
			throw new Exception(Messages.get().Role_41);
		}
		if (org.hasRole(rn)) {
			throw new Exception(Messages.get().Role_42);
		}

	}

	/**
	 * ������������
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().Role_43;
	}

	@Override
	public String getDefaultEditorId() {
		return EDITOR_DEFAULT;
	}
}
