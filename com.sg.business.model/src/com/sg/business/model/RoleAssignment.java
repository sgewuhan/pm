package com.sg.business.model;

import java.util.List;

import com.mobnut.db.model.IContext;
import com.mobnut.portal.user.UserSessionContext;
import com.sg.business.model.event.AccountEvent;
import com.sg.business.model.toolkit.UserToolkit;

/**
 * ��ɫ���û��Ĺ�ϵ
 * <p/>
 * ����ɫ����ĳ���û����û��ͽ�ɫΪ��Զ�Ĺ�ϵ
 * 
 * @author jinxitao
 * 
 */
public class RoleAssignment extends AbstractRoleAssignment {

	/**
	 * ������������
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "��ɫָ��";
	}

	/**
	 * ɾ����ɫָ��
	 */

	@Override
	public void doRemove(IContext context) throws Exception {
		// �˻�֪ͨ����
		String userId = getUserid();
		UserSessionContext.noticeAccountChanged(userId, new AccountEvent(
				AccountEvent.EVENT_ORG_CHANGED, this));

		super.doRemove(context);
	}

	@Override
	public boolean doSave(IContext context) throws Exception {
		String userid = getUserid();
		User user = UserToolkit.getUserById(userid);
		List<?> scenarios = (List<?>) user.getValue(User.F_SCENARIO);
		user.setValue(User.F_SCENARIO, scenarios);
		user.doSave(context);
		return super.doSave(context);
	}

	// @Override
	// public String getDefaultEditorId() {
	// return "editor.organization.roleassignment";
	// }
}
