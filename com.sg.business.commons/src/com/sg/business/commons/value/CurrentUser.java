package com.sg.business.commons.value;


import com.mobnut.portal.user.UserSessionContext;
import com.sg.widgets.part.editor.fields.value.IFieldDefaultValue;

/**
 * ����Ĭ����Ա
 * @author gdiyang
 *
 */
public class CurrentUser implements IFieldDefaultValue {

	public CurrentUser() {
	}

	/**
	 * ���ص�ǰ��¼�û�ID
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
