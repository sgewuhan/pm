package com.sg.business.message.view;

import com.mobnut.portal.user.IAccountEvent;
import com.sg.widgets.part.view.AccountSensitiveTableView;

public class Inbox extends AccountSensitiveTableView {

	@Override
	public void accountChanged(IAccountEvent event) {
		// ֻ��Ӧ���µ��¼�
		if (IAccountEvent.EVENT_CONSIGNER_CHANGED.equals(event.getEventCode())
				|| IAccountEvent.EVENT_MESSAGE.equals(event.getEventCode())) {
			super.accountChanged(event);
		}
	}


	/*
	 * BUG:10004 
	 * 
	 * ����:���治�Ѻ�
	 * 
	 * ����:������ʾ���¶�ȡ��Ϣ�б� 
	 * 
	 * ȥ����ʾ��ֱ��ˢ��
	 */
	@Override
	protected boolean isShowToastNotice() {
		return false;
	}
}
