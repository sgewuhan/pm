package com.sg.business.home.view;

import com.mobnut.portal.user.UserSessionContext;
import com.sg.widgets.part.view.AccountSensitiveTreeView;

/**
 * <P>
 * 公告板视图
 * </P>
 * 继承于{@link AccountSensitiveTreeView}
 * @author gdiyang
 *
 */
public class BulletinBoardView extends AccountSensitiveTreeView {

	/**
	 * 账户提示的信息Message
	 */
	@Override
	protected String getAccountNoticeMessage() {
		return "正在重新检索公告板数据...";
	}

	/**
	 * 设置发生哪些变动时相应：
	 * <li>人员变动
	 * <li>组织变动
	 */
	@Override
	public void accountChanged(Object data) {
		//只响应以下的事件
		if (UserSessionContext.EVENT_CONSIGNER_CHANGED.equals(data)
				|| UserSessionContext.EVENT_ORG_CHANGED.equals(data)) {
			super.accountChanged(data);
		}
	}
}
