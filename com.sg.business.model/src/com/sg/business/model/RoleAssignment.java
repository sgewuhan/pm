package com.sg.business.model;

import com.mobnut.db.model.IContext;
import com.mobnut.portal.user.UserSessionContext;
import com.sg.business.model.event.AccountEvent;


/**
 * ��ɫ���û��Ĺ�ϵ<p/>
 * ����ɫ����ĳ���û����û��ͽ�ɫΪ��Զ�Ĺ�ϵ
 * @author jinxitao
 *
 */
public class RoleAssignment extends AbstractRoleAssignment{

	/**
	 * ������������
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
		UserSessionContext.noticeAccountChanged(userId,  new AccountEvent(
				AccountEvent.EVENT_ORG_CHANGED, this));
		
		super.doRemove(context);
	}
}
