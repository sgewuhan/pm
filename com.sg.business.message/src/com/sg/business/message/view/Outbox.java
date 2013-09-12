package com.sg.business.message.view;

import com.mobnut.portal.user.IAccountEvent;
import com.sg.widgets.part.view.AccountSensitiveTableView;

public class Outbox extends AccountSensitiveTableView {

	@Override
	public void accountChanged(IAccountEvent event) {
		// ֻ��Ӧ���µ��¼�
		if (IAccountEvent.EVENT_CONSIGNER_CHANGED.equals(event.getEventCode())
				|| IAccountEvent.EVENT_MESSAGE.equals(event.getEventCode())) {
			super.accountChanged(event);
		}
	}

	@Override
	protected String getAccountNoticeMessage() {
		return "���¶�ȡ������Ϣ...";
	}


}
