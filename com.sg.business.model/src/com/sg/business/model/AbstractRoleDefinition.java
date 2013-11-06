package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

/**
 * ��ɫ����
 * <p/>
 * ����Ŀģ�����Ŀ�ж���Ľ�ɫ
 * 
 * @author zhonghua
 * 
 */
public abstract class AbstractRoleDefinition extends PrimaryObject {

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
	public static final String[] ROLE_ID_SYSTEM = new String[] {
			ROLE_PROJECT_MANAGER_ID, ROLE_PROJECT_GUEST_ID };

	/**
	 * ϵͳ��ɫ����
	 */
	public static final String[] ROLE_NAME_SYSTEM = new String[] {
			ROLE_PROJECT_MANAGER_TEXT, ROLE_PROJECT_GUEST_TEXT };

	/**
	 * ��ɫID
	 */
	public static final String F_ORGANIZATION_ROLE_ID = "role_id";

	/**
	 * ��ɫ���
	 */
	public static final String F_ROLE_NUMBER = "rolenumber";

	/**
	 * �жϽ�ɫ�Ƿ�Ϊ��֯��ɫ
	 * 
	 * @return boolean
	 */
	public boolean isOrganizatioRole() {
		return getValue(F_ORGANIZATION_ROLE_ID) != null;
	}

	/**
	 * ��ȡ��ɫ����ʾͼ��
	 * 
	 * @return Image
	 */
	public Image getImage() {
		if (isOrganizatioRole()) {
			return BusinessResource.getImage(BusinessResource.IMAGE_ROLE4_16);
		} else {
			return BusinessResource.getImage(BusinessResource.IMAGE_ROLE3_16);
		}
	}

	/**
	 * 
	 * ���ؽ�ɫ����֯��ɫ
	 * 
	 * @return
	 */
	public Role getOrganizationRole() {
		ObjectId id = getOrganizationRoleId();
		Assert.isNotNull(id);
		return ModelService.createModelObject(Role.class, id);
	}

	public ObjectId getOrganizationRoleId() {
		return (ObjectId) getValue(F_ORGANIZATION_ROLE_ID);
	}

	/**
	 * ��ɫ����ʾ����
	 * 
	 * @return String
	 */
	@Override
	public String getLabel() {
		if (isPersistent()) {
			if (isOrganizatioRole()) {
				Role role = getOrganizationRole();
				return role.getLabel();
			} else {
				return getDesc() + "|" + getRoleNumber();
			}
		} else {
			return "";
		}
	}

	/**
	 * ��ȡ��ɫ�ı��
	 * 
	 * @return String
	 */
	public String getRoleNumber() {
		return (String) getValue(F_ROLE_NUMBER);
	}

	/**
	 * �жϽ�ɫ�Ƿ�Ϊϵͳ��ɫ
	 * 
	 * @return boolean
	 */
	public boolean isSystemRole() {
		String rn = getRoleNumber();
		return Utils.inArray(rn, ROLE_ID_SYSTEM);
	}

	/**
	 * T��ͷ��P��ͷ����ϵͳԤ���Ľ�ɫ��
	 * 
	 * @param rn
	 * @return
	 */
	public boolean isReservedNumber(String rn) {
		return rn != null
				&& (rn.toUpperCase().startsWith("T0") || rn.toUpperCase()
						.startsWith("P0"));
	}

	/**
	 * ����ɫ����Ƿ�Ϸ�
	 * @throws Exception 
	 */
	public void check() throws Exception {
		
		// [bug:18] ��������
		String rn = getRoleNumber();
		if (isReservedNumber(rn)) {
			throw new Exception("����Ľ�ɫ�����ϵͳ�����ı��");
		}
		
	}

}
