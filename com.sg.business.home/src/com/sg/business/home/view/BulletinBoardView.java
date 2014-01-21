package com.sg.business.home.view;

import com.mobnut.portal.user.IAccountEvent;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.view.AccountSensitiveTreeView;

/**
 * <P>
 * 公告板视图
 * </P>
 * 继承于{@link AccountSensitiveTreeView}
 * 
 * @author gdiyang
 * 
 */
public class BulletinBoardView extends AccountSensitiveTreeView {

	/**
	 * 账户提示的信息Message
	 */
	@Override
	protected String getAccountNoticeMessage() {
		return Messages.get().BulletinBoardView_0;
	}

	/**
	 * 设置发生哪些变动时相应： <li>人员变动 <li>组织变动
	 */
	@Override
	public void accountChanged(IAccountEvent event) {
		String code = event.getEventCode();
		// 只响应以下的事件
		if (IAccountEvent.EVENT_CONSIGNER_CHANGED.equals(code)
				|| IAccountEvent.EVENT_ORG_CHANGED.equals(code)) {
			super.accountChanged(event);
		}
	}
}
