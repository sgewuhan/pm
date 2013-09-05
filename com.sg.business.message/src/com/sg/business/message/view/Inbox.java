package com.sg.business.message.view;

import com.mobnut.portal.user.UserSessionContext;
import com.sg.widgets.part.view.AccountSensitiveTableView;

public class Inbox extends AccountSensitiveTableView {

	@Override
	public void accountChanged(Object data) {
		//只响应以下的事件
		if (UserSessionContext.EVENT_CONSIGNER_CHANGED.equals(data)
				|| UserSessionContext.EVENT_MESSAGE.equals(data)
			) {
			super.accountChanged(data);
		}
	}

	@Override
	protected String getAccountNoticeMessage() {
		return "重新读取收件信息...";
	}


}
