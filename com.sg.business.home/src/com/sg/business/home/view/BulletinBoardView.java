package com.sg.business.home.view;

import com.mobnut.portal.user.UserSessionContext;
import com.sg.widgets.part.view.AccountSensitiveTreeView;

public class BulletinBoardView extends AccountSensitiveTreeView {

	@Override
	protected String getAccountNoticeMessage() {
		return "正在重新检索公告板数据...";
	}

	@Override
	public void accountChanged(Object data) {
		//只响应以下的事件
		if (UserSessionContext.EVENT_CONSIGNER_CHANGED.equals(data)
				|| UserSessionContext.EVENT_ORG_CHANGED.equals(data)) {
			super.accountChanged(data);
		}
	}
}
