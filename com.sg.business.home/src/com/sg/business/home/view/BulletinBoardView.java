package com.sg.business.home.view;

import com.mobnut.portal.user.UserSessionContext;
import com.sg.widgets.part.view.AccountSensitiveTreeView;

/**
 * <P>
 * �������ͼ
 * </P>
 * �̳���{@link AccountSensitiveTreeView}
 * @author gdiyang
 *
 */
public class BulletinBoardView extends AccountSensitiveTreeView {

	/**
	 * �˻���ʾ����ϢMessage
	 */
	@Override
	protected String getAccountNoticeMessage() {
		return "�������¼������������...";
	}

	/**
	 * ���÷�����Щ�䶯ʱ��Ӧ��
	 * <li>��Ա�䶯
	 * <li>��֯�䶯
	 */
	@Override
	public void accountChanged(Object data) {
		//ֻ��Ӧ���µ��¼�
		if (UserSessionContext.EVENT_CONSIGNER_CHANGED.equals(data)
				|| UserSessionContext.EVENT_ORG_CHANGED.equals(data)) {
			super.accountChanged(data);
		}
	}
}
