package com.sg.business.message.view;

import com.sg.widgets.part.view.AccountSensitiveTreeView;

public class MessageView extends AccountSensitiveTreeView {

	@Override
	protected String getAccountNoticeMessage() {
		return "正在更新我的收件箱消息列表...";
	}


}
