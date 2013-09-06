package com.sg.business.commons.value;

import org.bson.types.ObjectId;

import com.mobnut.portal.user.UserSessionContext;
import com.sg.business.model.User;
import com.sg.widgets.part.editor.fields.value.IFieldDefaultValue;

/**
 * 设置部门默认值
 * @author gdiyang
 *
 */
public class OrganizationDefault implements IFieldDefaultValue {

	public OrganizationDefault() {
	}

	/**
	 * 设置部门为当前登录用户所在部门的ID
	 */
	@Override
	public Object getDefaultValue(Object data, String key) {
		ObjectId orgId=null;
		try {
			String userId = UserSessionContext.getAccountInfo().getUserId();
			User user = User.getUserById(userId);
			orgId = user.getOrganization_id();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgId;
	}
}
