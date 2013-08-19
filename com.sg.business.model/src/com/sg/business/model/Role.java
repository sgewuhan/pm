package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;

/**
 * ��ɫ<p/>
 * ��֯�еĽ�ɫ
 * @author zhonghua
 *
 */
public class Role extends PrimaryObject {

	public static final String F_ORGANIZATION_ID = "organization_id";

	public static final String F_ROLE_NUMBER = "rolenumber";

	/**
	 * ������ɫ/��֯��ɫ/ϵͳ����Ա
	 */
	public static final String ROLE_ADMIN_ID = "T000";
	public static final String ROLE_ADMIN_TEXT = "ϵͳ����Ա";

	/**
	 * ������ɫ/��֯��ɫ/��֯����Ա
	 */
	public static final String ROLE_ORGANIZATION_ADMIN_ID = "T000";
	public static final String ROLE_ORGANIZATION_ADMIN_TEXT = "��֯����Ա";

	/**
	 * ������ɫ/��֯��ɫ/�ĵ�����Ա
	 */
	public static final String ROLE_VAULT_ADMIN_ID = "T001";
	public static final String ROLE_VALUT_ADMIN_TEXT = "�ĵ�����Ա";

	/**
	 * ������ɫ/��֯��ɫ/��Ŀ����Ա
	 */
	public static final String ROLE_PROJECT_ADMIN_ID = "T002";
	public static final String ROLE_PROJECT_ADMIN_TEXT = "��Ŀ����Ա";

	/**
	 * ������ɫ/��֯��ɫ/ҵ�����Ա
	 */
	public static final String ROLE_BUSINESS_ADMIN_ID = "T004";
	public static final String ROLE_BUSINESS_ADMIN_TEXT = "ҵ�����Ա";
	
	/**
	 * ������ɫ/��֯��ɫ/�ĵ�������
	 */
	public static final String ROLE_VAULT_GUEST_ID = "T003";
	public static final String ROLE_VAULT_GUEST_TEXT = "�ĵ�������";

	/**
	 * ������ɫ/��Ŀ��ɫ/��Ŀ����
	 */
	public static final String ROLE_PROJECT_MANAGER_ID = "P000";
	public static final String ROLE_PROJECT_MANAGER_TEXT = "��Ŀ����";

	/**
	 * ������ɫ/��Ŀ��ɫ/��Ŀ������
	 */
	public static final String ROLE_PROJECT_GUEST_ID = "P001";
	public static final String ROLE_PROJECT_GUEST_TEXT = "��Ŀ������";

	/**
	 * ϵͳ��ɫID
	 */
	public static final String[] ROLE_ID_SYSTEM = new String[] { ROLE_ADMIN_ID,
			ROLE_ORGANIZATION_ADMIN_ID, ROLE_PROJECT_ADMIN_ID,ROLE_BUSINESS_ADMIN_ID,
			ROLE_PROJECT_GUEST_ID, ROLE_PROJECT_MANAGER_ID, ROLE_VAULT_ADMIN_ID,
			ROLE_VAULT_GUEST_ID };

	/**
	 * ϵͳ��ɫ����
	 */
	public static final String[] ROLE_NAME_SYSTEM = new String[] {
			ROLE_ADMIN_TEXT, ROLE_ORGANIZATION_ADMIN_TEXT,ROLE_BUSINESS_ADMIN_TEXT,
			ROLE_PROJECT_ADMIN_TEXT, ROLE_PROJECT_GUEST_TEXT,
			ROLE_PROJECT_MANAGER_TEXT, ROLE_VALUT_ADMIN_TEXT,
			ROLE_VAULT_GUEST_TEXT };

	/**
	 * ��ɫ��ϵͳ�еĵ���ʾ����
	 */
	@Override
	public String getLabel() {
		return getDesc() + "|" + getRoleNumber();
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
	 * ��ȡ��ɫ������֯��ID
	 * @return ObjectId
	 */
	public ObjectId getOrganization_id() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	/**
	 * ����û�����ɫ��
	 * @param users
	 *          ���û�����
	 */
	public void doAssignUsers(List<PrimaryObject> users) {
		DBCollection roleAssignmentCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ROLE_ASSIGNMENT);
		List<DBObject> list = new ArrayList<DBObject>();

		for (int i = 0; i < users.size(); i++) {
			User user = (User) users.get(i);
			UserSessionContext.noticeAccountChanged(user.getUserid(),
					UserSessionContext.EVENT_ROLE_CHANGED);
			list.add(new BasicDBObject()
					.append(RoleAssignment.F__TYPE,IModelConstants.C_ROLE_ASSIGNMENT)
					.append(RoleAssignment.F_USER_ID, user.getUserid())
					.append(RoleAssignment.F_USER_NAME, user.getUsername())
					.append(RoleAssignment.F_ROLE_ID, get_id())
					.append(RoleAssignment.F_ROLE_NUMBER, getRoleNumber())
					.append(RoleAssignment.F_ROLE_NAME, getDesc()));
		}
		roleAssignmentCol.insert(list);

	}

	/**
	 * ɾ����ɫ
	 * @param context
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		// ��ɾ����ɫָ��
		DBCollection raCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE_ASSIGNMENT);
		raCol.remove(new BasicDBObject().append(RoleAssignment.F_ROLE_ID,
				get_id()));
		super.doRemove(context);
	}

	/**
	 * ��ȡ��ɫ���
	 * @return String
	 */
	public String getRoleNumber() {
		return (String) getValue(F_ROLE_NUMBER);
	}

	/**
	 * ��ý�ɫ��������֯
	 * 
	 * @return  Organization
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
	 * ��ȡ��ɫ����
	 * @return
	 */
	public String getRoleTypeText() {
		if (isSystemRole()) {
			return "ϵͳ��ɫ";
		} else {
			return "�û���ɫ";
		}
	}

	/**
	 * �жϽ�ɫ�Ƿ���Ը���
	 * @param context
	 */
	@Override
	public boolean canEdit(IContext context) {
		// ϵͳ�Ľ�ɫ�����Ը���
		if (isSystemRole()) {
			return false;
		}
		return super.canEdit(context);
	}

	/**
	 * �жϽ�ɫ�Ƿ����ɾ��
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
	 * @return boolean
	 */
	public boolean isSystemRole() {
		String rn = getRoleNumber();
		if (Utils.inArray(rn, ROLE_ID_SYSTEM)) {
			// �������֯����������صĽ�ɫ
			if (ROLE_VAULT_ADMIN_ID.equals(rn) || ROLE_VAULT_GUEST_ID.equals(rn)) {
				// �жϸ���֯�Ƿ�������
				Organization org = getOrganization();
				if (Boolean.TRUE.equals(org.isContainer())) {
					return true;
				} else {
					return false;
				}
			} else if (ROLE_PROJECT_ADMIN_ID.equals(rn)|| ROLE_BUSINESS_ADMIN_ID.equals(rn)) {
				// �жϸ���֯�Ƿ������Ŀ����ְ��
				Organization org = getOrganization();
				if (Boolean.TRUE.equals(org.isFunctionDepartment())) {
					return true;
				} else {
					return false;
				}
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
				&& (rn.toUpperCase().startsWith("T") || rn.toUpperCase()
						.startsWith("P"));
	}

	public void check() throws Exception {
		String rn = getRoleNumber();
		// ����ɫ����Ƿ�Ϸ�
		if (isReservedNumber(rn)) {
			throw new Exception("����Ľ�ɫ�����ϵͳ�����ı��");
		}
		// ����ɫ����ڵ�ǰ��֯���Ƿ����
		Organization org = getOrganization();
		if (org == null) {
			throw new Exception("��ɫ��������֯�Ѳ�����");
		}
		if (org.hasRole(rn)) {
			throw new Exception("��ɫ�������������֯���ظ�");
		}

	}
}
