package com.sg.business.model;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

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
	 * 已读标志,DBObject类型字段,保存了用户ID，是否已读<br/>{"zhonghua": true,"zhansan":true}
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
	 * 目标列表 {{targetid=ObjectId(""),targetclass="com.sg.business.model.Work",targeteditor="work.editor"},{}}
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
		reply.setValue(F_DESC, "RE:"+getDesc());
		return reply;
	}


	public void doMarkRead(IContext context, Boolean isRead) throws Exception {
        Object markReadData = getValue(F_MARK_READ);
        if(!(markReadData instanceof DBObject)){
        	markReadData=new BasicDBObject();
        }
        ((DBObject)markReadData).put(context.getAccountInfo().getUserId(), isRead);
		setValue(F_MARK_READ, markReadData);
		doSave(context);
	}


	public Boolean isRead(IContext context) {
		 Object markReadData = getValue(F_MARK_READ);
	        if(!(markReadData instanceof DBObject)){
	        	return false;
	        }
	       return (Boolean) ((DBObject)markReadData).get(context.getAccountInfo().getUserId());
	}

}
