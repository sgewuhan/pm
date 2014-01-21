package com.sg.business.home.view;

import com.mobnut.portal.user.IAccountEvent;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.view.AccountSensitiveTreeView;

/**
 * <P>
 * �������ͼ
 * </P>
 * �̳���{@link AccountSensitiveTreeView}
 * 
 * @author gdiyang
 * 
 */
public class BulletinBoardView extends AccountSensitiveTreeView {

	/**
	 * �˻���ʾ����ϢMessage
	 */
	@Override
	protected String getAccountNoticeMessage() {
		return Messages.get().BulletinBoardView_0;
	}

	/**
	 * ���÷�����Щ�䶯ʱ��Ӧ�� <li>��Ա�䶯 <li>��֯�䶯
	 */
	@Override
	public void accountChanged(IAccountEvent event) {
		String code = event.getEventCode();
		// ֻ��Ӧ���µ��¼�
		if (IAccountEvent.EVENT_CONSIGNER_CHANGED.equals(code)
				|| IAccountEvent.EVENT_ORG_CHANGED.equals(code)) {
			super.accountChanged(event);
		}
	}
}
