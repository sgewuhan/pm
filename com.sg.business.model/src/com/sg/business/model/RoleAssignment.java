package com.sg.business.model;

import com.mobnut.db.model.IContext;
import com.mobnut.portal.user.UserSessionContext;
import com.sg.business.model.event.AccountEvent;


/**
 * 角色和用户的关系<p/>
 * 将角色赋予某个用户，用户和角色为多对多的关系
 * @author jinxitao
 *
 */
public class RoleAssignment extends AbstractRoleAssignment{

	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "角色指派";
	}
	/**
	 * 删除角色指派
	 */

	@Override
	public void doRemove(IContext context) throws Exception {
		// 账户通知处理
		String userId = getUserid();
		UserSessionContext.noticeAccountChanged(userId,  new AccountEvent(
				AccountEvent.EVENT_ORG_CHANGED, this));
		
		super.doRemove(context);
	}
}
