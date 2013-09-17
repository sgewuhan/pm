package com.sg.business.message.view;

import com.mobnut.portal.user.IAccountEvent;
import com.sg.widgets.part.view.AccountSensitiveTableView;

public class Outbox extends AccountSensitiveTableView {

	@Override
	public void accountChanged(IAccountEvent event) {
		// 只响应以下的事件
		if (IAccountEvent.EVENT_CONSIGNER_CHANGED.equals(event.getEventCode())
				|| IAccountEvent.EVENT_MESSAGE.equals(event.getEventCode())) {
			super.accountChanged(event);
		}
	}

	
	/*
	 * BUG:10004 
	 * 
	 * 类型:界面不友好
	 * 
	 * 描述:经常提示重新读取消息列表 
	 * 
	 * 去掉提示，直接刷新
	 */
	@Override
	protected boolean isShowToastNotice() {
		return false;
	}
	
//	@Override
//	protected String getAccountNoticeMessage() {
//		return "重新读取发件信息...";
//	}


}
