package com.sg.business.message.view;

import com.sg.widgets.part.view.AccountSensitiveTreeView;

public class MessageView extends AccountSensitiveTreeView {

	@Override
	protected String getAccountNoticeText() {
		return "您收到新的消息";
	}

	@Override
	protected String getAccountNoticeMessage() {
		return "正在更新消息列表...";
	}


}
