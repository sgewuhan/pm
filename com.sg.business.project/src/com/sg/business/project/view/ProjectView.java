package com.sg.business.project.view;

import com.mobnut.portal.user.UserSessionContext;
import com.sg.widgets.part.view.AccountSensitiveTableView;

public class ProjectView extends AccountSensitiveTableView {

	@Override
	protected String getAccountNoticeMessage() {
		return "�������¼�����Ŀ����...";
	}

	@Override
	public void accountChanged(Object data) {
		//ֻ��Ӧ���µ��¼�
		if (UserSessionContext.EVENT_CONSIGNER_CHANGED.equals(data)
				|| UserSessionContext.EVENT_ORG_CHANGED.equals(data)
				|| UserSessionContext.EVENT_ROLE_CHANGED.equals(data)) {
			super.accountChanged(data);
		}
	}

}
