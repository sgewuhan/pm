package com.sg.business.home.view;

import com.mobnut.portal.user.UserSessionContext;
import com.sg.widgets.part.view.AccountSensitiveTreeView;

public class BulletinBoardView extends AccountSensitiveTreeView {

	@Override
	protected String getAccountNoticeMessage() {
		return "�������¼������������...";
	}

	@Override
	public void accountChanged(Object data) {
		//ֻ��Ӧ���µ��¼�
		if (UserSessionContext.EVENT_CONSIGNER_CHANGED.equals(data)
				|| UserSessionContext.EVENT_ORG_CHANGED.equals(data)) {
			super.accountChanged(data);
		}
	}
}
