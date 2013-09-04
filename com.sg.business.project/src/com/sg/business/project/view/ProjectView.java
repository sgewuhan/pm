package com.sg.business.project.view;

import com.mobnut.portal.user.UserSessionContext;
import com.sg.widgets.part.view.AccountSensitiveTableView;

public class ProjectView extends AccountSensitiveTableView {

	@Override
	protected String getAccountNoticeMessage() {
		return "正在重新检索项目数据...";
	}

	@Override
	public void accountChanged(Object data) {
		//只响应以下的事件
		if (UserSessionContext.EVENT_CONSIGNER_CHANGED.equals(data)
				|| UserSessionContext.EVENT_ORG_CHANGED.equals(data)
				|| UserSessionContext.EVENT_ROLE_CHANGED.equals(data)) {
			super.accountChanged(data);
		}
	}

}
