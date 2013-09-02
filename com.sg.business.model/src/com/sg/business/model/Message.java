package com.sg.business.model;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Message extends PrimaryObject {
	
	/**
	 * �ϼ�����Ϣid
	 */
	public static final String F_PARENT_MESSAGE = "parent_id";
	
	/**
	 * �������ֶΣ�userid
	 */
	public static final String F_SENDER = "sender";
	
	/**
	 * ������
	 */
	public static final String F_RECIEVER = "reciever";
	
	/**
	 * �����ֶ�
	 */
	public static final String F_DESC = "desc";
	
	/**
	 * ����
	 */
	public static final String F_CONTENT = "content";

	/**
	 * ��������
	 */
	public static final String F_SENDDATE = "senddate";
	
	/**
	 * ��������
	 */
	public static final String F_RECIEVEDDATE = "recievedate";
	
	/**
	 * �Ѷ���־,DBObject�����ֶ�,�������û�ID���Ƿ��Ѷ�<br/>{"zhonghua": true,"zhansan":true}
	 */
	public static final String F_MARK_READ = "markread";
	
	/**
	 * ��Ҫ��
	 */
	public static final String F_IMPORTANCE = "importance";
	
	/**
	 * �����ʼ����ѷ�������
	 */
	public static final String F_EMAIL_NOTICE_DATE = "emailnoticedate";
	
	/**
	 * Ŀ���б� {{targetid=ObjectId(""),targetclass="com.sg.business.model.Work",targeteditor="work.editor"},{}}
	 */
	public static final String F_TARGETS = "targets";
	
	/**
	 * Ŀ�����
	 */
	public static final String SF_TARGET = "targetid";
	
	/**
	 * Ŀ����
	 */
	public static final String SF_TARGET_CLASS = "targetclass";
	
	/**
	 * Ŀ��༭��
	 */
	public static final String SF_TARGET_EDITOR = "targeteditor";
	
	
	/**
	 * ����,�ļ��б����ֶ�
	 */
	public static final String F_ATTACHMENT = "attachment";

	/**
	 * �ظ��༭��ID
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
