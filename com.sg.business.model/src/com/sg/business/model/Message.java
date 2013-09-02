package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;

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
	public static final String F_TITLE = "title";
	
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
	 * 已读标志
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
	 * 目标列表
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

}
