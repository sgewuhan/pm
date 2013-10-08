package com.sg.business.message.view;

import com.mobnut.admin.dataset.Setting;
import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.part.view.AutoRefreshableTableView;

public class Inbox extends AutoRefreshableTableView {

	// @Override
	// public void accountChanged(IAccountEvent event) {
	// // 只响应以下的事件
	// if (IAccountEvent.EVENT_CONSIGNER_CHANGED.equals(event.getEventCode())
	// || IAccountEvent.EVENT_MESSAGE.equals(event.getEventCode())) {
	// super.accountChanged(event);
	// }
	// }
	//
	//
	// /*
	// * BUG:10004
	// *
	// * 类型:界面不友好
	// *
	// * 描述:经常提示重新读取消息列表
	// *
	// * 去掉提示，直接刷新
	// */
	// @Override
	// protected boolean isShowToastNotice() {
	// return false;
	// }

	@Override
	protected int getInterval() {
		IContext context = getContext();
		Object val = Setting.getUserSetting(context.getAccountInfo().getUserId(),
				IModelConstants.S_U_MESSAGE_RESERVED_REFRESH_INTERVAL);
		Integer value = Utils.getIntegerValue(val);
		if(value!=null){
			return value.intValue()*60000;//分钟为单位
		}else{
			return  600000;//间隔10分钟刷新
		}
	}
}
