package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

/**
 * ��ɫ����<p/>
 * ����Ŀģ�����Ŀ�ж���Ľ�ɫ
 * @author zhonghua
 *
 */
public class AbstractRoleDefinition extends PrimaryObject{

	/**
	 * ��ɫID
	 */
	public static final String F_ORGANIZATION_ROLE_ID = "role_id";

	/**
	 * ��ɫ���
	 */
	public static final String F_ROLE_NUMBER = "rolenumber";

	/**
	 * �༭��ɫ�ı༭��
	 */
	public static final String EDITOR_ROLE_DEFINITION_CREATE = "editor.roleDefinition";

	/**
	 * �жϽ�ɫ�Ƿ�Ϊ��֯��ɫ
	 * @return boolean
	 */
	public boolean isOrganizatioRole() {
		return getValue(F_ORGANIZATION_ROLE_ID) != null;
	}

	/**
	 * ��ȡ��ɫ����ʾͼ��
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
	 * ��ȡ��֯��ɫ�������֯�в����ڴ˽�ɫ����ʹ���
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
	 * ��ɫ����ʾͼ��
	 * @return String
	 */
	@Override
	public String getLabel() {
		if (isOrganizatioRole()) {
			Role role = getOrganizationRole();
			return role.getLabel();
		} else {
			return getDesc() + "|" + getRoleNumber();
		}
	}

	/**
	 * ��ȡ��ɫ�ı��
	 * @return String
	 */
	public String getRoleNumber() {
		return (String) getValue(F_ROLE_NUMBER);
	}

}
