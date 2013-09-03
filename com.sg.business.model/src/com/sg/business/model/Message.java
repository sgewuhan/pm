package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.CurrentAccountContext;

public class Message extends PrimaryObject {

	/**
	 * 上级的消息id
	 */
	public static final String F_PARENT_MESSAGE = "parent_id";

	/**
	 * 发送者字段，userid
	 */
	public static final String F_SENDER = "sender";

	/**
	 * 接收者
	 */
	public static final String F_RECIEVER = "reciever";

	/**
	 * 标题字段
	 */
	public static final String F_DESC = "desc";

	/**
	 * 内容
	 */
	public static final String F_CONTENT = "content";

	/**
	 * 发送日期
	 */
	public static final String F_SENDDATE = "senddate";

	/**
	 * 接收日期
	 */
	public static final String F_RECIEVEDDATE = "recievedate";

	/**
	 * 已读标志,DBObject类型字段,保存了用户ID，是否已读<br/>
	 * {"zhonghua": true,"zhansan":true}
	 */
	public static final String F_MARK_READ = "markread";

	/**
	 * 重要度
	 */
	public static final String F_IMPORTANCE = "importance";

	/**
	 * 电子邮件提醒发送日期
	 */
	public static final String F_EMAIL_NOTICE_DATE = "emailnoticedate";

	/**
	 * 目标列表 {{targetid=ObjectId(""),targetclass="com.sg.business.model.Work",
	 * targeteditor="work.editor"},{}}
	 */
	public static final String F_TARGETS = "targets";

	/**
	 * 目标对象
	 */
	public static final String SF_TARGET = "targetid";

	/**
	 * 目标类
	 */
	public static final String SF_TARGET_CLASS = "targetclass";

	/**
	 * 目标编辑器
	 */
	public static final String SF_TARGET_EDITOR = "targeteditor";

	/**
	 * 附件,文件列表型字段
	 */
	public static final String F_ATTACHMENT = "attachment";

	/**
	 * 回复编辑器ID
	 */
	public static final String EDITOR_REPLY = "message.editor.reply";

	public Message makeReply() {
		Message reply = ModelService.createModelObject(Message.class);
		reply.setValue(F_PARENT_MESSAGE, get_id());
		reply.setValue(F_RECIEVER, getValue(F_SENDER));
		reply.setValue(F_DESC, "RE:" + getDesc());
		return reply;
	}

	public void doMarkRead(IContext context, Boolean isRead) throws Exception {
		Object markReadData = getValue(F_MARK_READ);
		if (!(markReadData instanceof DBObject)) {
			markReadData = new BasicDBObject();
		}
		((DBObject) markReadData).put(context.getAccountInfo().getUserId(),
				isRead);
		setValue(F_MARK_READ, markReadData);
		doSave(context);
	}

	public Boolean isRead(IContext context) {
		Object markReadData = getValue(F_MARK_READ);
		if (!(markReadData instanceof DBObject)) {
			return false;
		}
		return (Boolean) ((DBObject) markReadData).get(context.getAccountInfo()
				.getUserId());
	}

	@Override
	public String getHTMLLabel() {
		boolean isRead = isRead(new CurrentAccountContext());
		StringBuffer sb = new StringBuffer();
		String imageUrl = "<img src='" +(isRead?getImageURLForOpen():getImageURL())
				+ "' style='float:left;padding:2px' width='24' height='24' />";
		String label = getLabel();
		String senderId = (String) getValue(F_SENDER);
		User sender = User.getUserById(senderId);

		SimpleDateFormat sdf = new SimpleDateFormat(
				Utils.SDF_DATE_COMPACT_SASH);
		Date date = (Date) getValue(F_SENDDATE);
		String sendDate = sdf.format(date);
		sb.append(imageUrl);
		if (isRead) {
			sb.append(label);
		} else {
			sb.append("<b>");
			sb.append(label);
			sb.append("</b>");
		}
		sb.append("<br/>");
		sb.append("发件人:" + sender + " " + sendDate);

		return sb.toString();
	}

	public String getStatus() {
		if (isRead(new CurrentAccountContext())) {
			return "marked";
		} else
			return "message";

	}
	public String getImageURLForReply() {
			return FileUtil.getImageURL(
					BusinessResource.IMAGE_MESSAGE_REPLY_24,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	}
	
	public String getImageURLForOpen() {
		return FileUtil.getImageURL(
				BusinessResource.IMAGE_MESSAGE_OPEN_24,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
}
	public String getImageURL() {
		return FileUtil.getImageURL(
				BusinessResource.IMAGE_MESSAGE_24,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
}
	

	public String getHTMLLabelForSend() {
		boolean isReply=isReply();
		StringBuffer sb = new StringBuffer();
		String imageUrl = "<img src='" + (isReply?getImageURLForReply():getImageURL())
				+ "' style='float:left;padding:2px' width='24' height='24' />";
		String label = getLabel();
		SimpleDateFormat sdf = new SimpleDateFormat(
				Utils.SDF_DATE_COMPACT_SASH);
		Date date = (Date) getValue(F_SENDDATE);
		String sendDate = sdf.format(date);
		sb.append(label);
		sb.append("<br/>");
		sb.append(imageUrl);
			String senderId = (String) getValue(F_SENDER);
			User sender = User.getUserById(senderId);
			sb.append("发件人:" + sender + " " + sendDate );
			sb.append("<br/>");
/*			String recieverId = (String) getValue(F_RECIEVER);
			User reciever = User.getUserById(recieverId);
			sb.append("收件人:" + reciever);*/
		return sb.toString();
	}

	public  boolean isReply() {
		return getValue(F_PARENT_MESSAGE)!=null;
	}

}
