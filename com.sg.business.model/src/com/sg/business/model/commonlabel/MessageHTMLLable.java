package com.sg.business.model.commonlabel;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mobnut.commons.util.Utils;
import com.mobnut.portal.user.UserSessionContext;
import com.sg.business.model.Message;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;
import com.sg.widgets.part.CurrentAccountContext;

public class MessageHTMLLable extends CommonHTMLLabel{

	private Message message;

	public MessageHTMLLable(Message message) {
		this.message = message;
	}

	@Override
	public String getHTML() {
		boolean isRead = message.isRead(new CurrentAccountContext());
		String userid = ""; //$NON-NLS-1$
		try {
			userid = UserSessionContext.getAccountInfo().getConsignerId();
		} catch (Exception e) {
		}
		StringBuffer sb = new StringBuffer();

		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>"); //$NON-NLS-1$

		// 添加图标
		String imageUrl = null;
		// 如果为已读消息，显示图标地址为getImageURLForOpen()
		if (isRead ) {
			imageUrl = "<img src='" //$NON-NLS-1$
					+ message.getImageURLForOpen()
					+ "' style='float:left;padding:6px' width='24' height='24' />"; //$NON-NLS-1$
		} else {
			imageUrl = "<img src='" //$NON-NLS-1$
					+ message.getImageURL()
					+ "' style='float:left;padding:6px' width='24' height='24' />"; //$NON-NLS-1$
		}
		sb.append(imageUrl);

		// 添加主题
		String label = message.getLabel();
		label = Utils.getPlainText(label);
		label = Utils.getLimitLengthString(label, 40);
		if (isRead || message.getValue(Message.F_SENDER).equals(userid)) {
			sb.append(label);
		} else {
			sb.append("<b>" + label + "</b>"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		sb.append("</span><br/>"); //$NON-NLS-1$

		sb.append("<small>"); //$NON-NLS-1$
		// 添加日期
		SimpleDateFormat sdf = new SimpleDateFormat(
				Utils.SDF_DATETIME_COMPACT_SASH);
		Date date = (Date) message.getValue(Message.F_SENDDATE);
		String sendDate = sdf.format(date);
		sb.append(sendDate);
		sb.append("</small>"); //$NON-NLS-1$

		return sb.toString();

	}
	
	
	
}
