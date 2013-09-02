package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;

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
	public static final String F_TITLE = "title";
	
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
	 * �Ѷ���־
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
	 * Ŀ���б�
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

}
