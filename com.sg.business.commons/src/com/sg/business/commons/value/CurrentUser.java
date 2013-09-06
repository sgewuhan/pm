package com.sg.business.commons.value;


import com.mobnut.portal.user.UserSessionContext;
import com.sg.widgets.part.editor.fields.value.IFieldDefaultValue;

/**
 * 设置默认人员
 * @author gdiyang
 *
 */
public class CurrentUser implements IFieldDefaultValue {

	public CurrentUser() {
	}

	/**
	 * 返回当前登录用户ID
	 */
	@Override
	public Object getDefaultValue(Object data, String key) {
		String userId=null;
		try {
			userId = UserSessionContext.getAccountInfo().getUserId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userId;
	}

}
