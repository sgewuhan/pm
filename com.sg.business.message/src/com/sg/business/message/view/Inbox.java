package com.sg.business.message.view;

import com.mobnut.portal.user.IAccountEvent;
import com.sg.widgets.part.view.AccountSensitiveTableView;

public class Inbox extends AccountSensitiveTableView {

	@Override
	public void accountChanged(IAccountEvent event) {
		// 只响应以下的事件
		if (IAccountEvent.EVENT_CONSIGNER_CHANGED.equals(event.getEventCode())
				|| IAccountEvent.EVENT_MESSAGE.equals(event.getEventCode())) {
			super.accountChanged(event);
		}
	}

	@Override
	protected String getAccountNoticeMessage() {
		return "重新读取收件信息...";
	}

}
