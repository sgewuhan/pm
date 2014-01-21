package com.sg.business.project.view;

import com.mobnut.portal.user.IAccountEvent;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.view.AccountSensitiveTableView;

public class ProjectView extends AccountSensitiveTableView {

	@Override
	protected String getAccountNoticeMessage() {
		return Messages.get().ProjectView_0;
	}

	@Override
	public void accountChanged(IAccountEvent event) {
		String code = event.getEventCode();
		// 只响应以下的事件
		if (IAccountEvent.EVENT_CONSIGNER_CHANGED.equals(code)
				|| IAccountEvent.EVENT_ORG_CHANGED.equals(code)
				|| IAccountEvent.EVENT_ROLE_CHANGED.equals(code)) {
			super.accountChanged(event);
		}
	}

}
