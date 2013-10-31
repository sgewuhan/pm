package com.sg.business.work.view;

import org.eclipse.swt.widgets.Composite;

import com.mobnut.portal.user.UserSessionContext;
import com.sg.widgets.part.view.TableNavigator;

public class WorkFlowReservedTask extends TableNavigator {

	@Override
	public void createPartControl(Composite parent) {
		String userId = getContext().getAccountInfo().getConsignerId();
		UserSessionContext.getSession().noticeUI(parent.getDisplay(), userId,
				"perspective.project", 2, 1, "您有两条未读消息");
		super.createPartControl(parent);
	}
}
