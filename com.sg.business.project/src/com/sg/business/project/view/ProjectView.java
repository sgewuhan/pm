package com.sg.business.project.view;

import com.mobnut.portal.user.IAccountEvent;
import com.sg.widgets.part.view.AccountSensitiveTableView;

public class ProjectView extends AccountSensitiveTableView {

	@Override
	protected String getAccountNoticeMessage() {
		return "正在重新检索项目数据...";
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
