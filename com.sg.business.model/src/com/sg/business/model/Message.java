package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.BasicBSONList;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.CurrentAccountContext;

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
	 * �Ѷ���־,DBObject�����ֶ�,�������û�ID���Ƿ��Ѷ�<br/>
	 * {"zhonghua": true,"zhansan":true}
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
	 * Ŀ���б� {{targetid=ObjectId(""),targetclass="com.sg.business.model.Work",
	 * targeteditor="work.editor"},{}}
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
	 * �Ƿ�����޸�
	 */
	public static final String SF_TARGET_EDITABLE = "targeteditable";

	/**
	 * ����,�ļ��б����ֶ�
	 */
	public static final String F_ATTACHMENT = "attachment";

	/**
	 * �ظ��༭��ID
	 */
	public static final String EDITOR_REPLY = "message.editor.reply";

	/**
	 * ����Ϣ�༭��
	 */
	public static final String EDITOR_SEND = "message.editor.create";

	/**
	 * ��Ϣ�鿴�༭��
	 */
	public static final String EDITOR_VIEW = "message.editor.view";

	/**
	 * ʹ��html������
	 */
	public static final String F_ISHTMLBODY = "ishtmlcontent";

	/**
	 * �����ظ���Ϣ����
	 * 
	 * @return Message
	 */
	public Message makeReply() {
		Message reply = ModelService.createModelObject(Message.class);
		reply.setValue(F_PARENT_MESSAGE, get_id());
		// ���ý�����
		Object value = getValue(F_SENDER);
		BasicDBList recieverList = new BasicDBList();
		recieverList.add(value);
		reply.setValue(F_RECIEVER, recieverList);
		reply.setValue(F_DESC, "RE:" + getDesc());
		return reply;
	}

	/**
	 * ������Ϣ״̬
	 * 
	 * @param context
	 * @param isRead
	 *            ,�Ƿ��Ѷ�
	 * @throws Exception
	 */
	public void doMarkRead(IContext context, Boolean isRead) throws Exception {
		Object markReadData = getValue(F_MARK_READ);
		if (!(markReadData instanceof DBObject)) {
			markReadData = new BasicDBObject();
		}
		((DBObject) markReadData).put(context.getAccountInfo().getUserId(),
				isRead);
		setValue(F_MARK_READ, markReadData);
		setValue(F_RECIEVEDDATE, new Date());
		doSave(context);
	}

	public boolean isRead(IContext context) {
		Object markReadData = getValue(F_MARK_READ);
		if (!(markReadData instanceof DBObject)) {
			return false;
		}
		String userId = context.getAccountInfo().getUserId();
		return Boolean.TRUE.equals(((DBObject) markReadData).get(userId));
	}

	/**
	 * ���ػظ�ͼ��·��
	 * 
	 * @return String
	 */
	public String getImageURLForReply() {
		return FileUtil.getImageURL(BusinessResource.IMAGE_MESSAGE_REPLY_24,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	}

	/**
	 * �����Ѷ���Ϣͼ��·��
	 * 
	 * @return String
	 */
	public String getImageURLForOpen() {
		return FileUtil.getImageURL(BusinessResource.IMAGE_MESSAGE_OPEN_24,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	}

	/**
	 * ����δ����Ϣͼ��·��
	 * 
	 * @return String
	 */
	public String getImageURL() {
		return FileUtil.getImageURL(BusinessResource.IMAGE_MESSAGE_24,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	}

	/**
	 * ���ر������ʾ����
	 * 
	 * @return String
	 */
	public String getHTMLLabel() {
		boolean isReply = isReply();
		boolean isRead = isRead(new CurrentAccountContext());
		StringBuffer sb = new StringBuffer();

		// �������
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE_COMPACT_SASH);
		Date date = (Date) getValue(F_SENDDATE);
		String sendDate = sdf.format(date);
		sb.append("<span style='float:right;padding-right:4px'>");
		sb.append(sendDate);
		sb.append("</span>");

		// ���ͼ��
		String imageUrl = null;
		// ���Ϊ�Ѷ���Ϣ����ʾͼ���ַΪgetImageURLForOpen()
		if (isRead) {
			imageUrl = "<img src='"
					+ getImageURLForOpen()
					+ "' style='float:left;padding:6px' width='24' height='24' />";
		} else {
			imageUrl = "<img src='"
					+ (isReply ? getImageURLForReply() : getImageURL())
					+ "' style='float:left;padding:6px' width='24' height='24' />";
		}
		sb.append(imageUrl);

		// �������
		String label = getLabel();
		label = Utils.getPlainText(label);
		label = Utils.getLimitLengthString(label, 20);
		if (isRead) {
			sb.append(label);
		} else {
			sb.append("<b>" + label + "</b>");
		}

		sb.append("<br/>");

		String senderId = (String) getValue(F_SENDER);
		User sender = User.getUserById(senderId);
		sb.append("������:" + sender);
		sb.append("  ");
		String recieverLabel = getRecieverLabel();
		sb.append("�ռ���:" + recieverLabel);
		return sb.toString();
	}

	/**
	 * ���ؽ����˵���ʾ����
	 * 
	 * @return String
	 */
	public String getRecieverLabel() {
		String result = "";
		Object value = getValue(F_RECIEVER);
		if (value instanceof BasicBSONList) {
			BasicBSONList recieverList = (BasicBSONList) value;
			if (recieverList.size() > 0) {
				String userId = (String) recieverList.get(0);
				User user = User.getUserById(userId);
				result = user.getLabel();
				// ����ռ����ж���ˣ�����ʾ��һ���ռ��˼�ʡ�Ժ�
				if (recieverList.size() > 1) {
					result += " ...";
				}
			}
		}

		return result;
	}

	/**
	 * �ж��Ƿ�Ϊ�ظ���Ϣ
	 * 
	 * @return boolean
	 */
	public boolean isReply() {
		return getValue(F_PARENT_MESSAGE) != null;
	}

	/**
	 * ���뷢���˺ͷ�������
	 */
	@Override
	public void doInsert(IContext context) throws Exception {
		Object value = getValue(F_RECIEVER);
		if (value instanceof BasicBSONList) {
			setValue(F_SENDDATE, new Date());
			setValue(F_SENDER, context.getAccountInfo().getUserId());
			super.doInsert(context);

			// �����˻�֪ͨ
			BasicBSONList recieverList = (BasicBSONList) value;
			for (int i = 0; i < recieverList.size(); i++) {
				UserSessionContext.noticeAccountChanged((String)recieverList.get(i),
						UserSessionContext.EVENT_MESSAGE);
			}

		} else {
			throw new Exception("ȱ���ռ���");
		}

	}

	/**
	 * ����Ϣ����Ŀ�굼������
	 * 
	 * @param target
	 */
	public void appendTargets(PrimaryObject target, String editorId,
			boolean editable) {
		Object value = getValue(F_TARGETS);
		if (!(value instanceof BasicBSONList)) {
			value = new BasicDBList();
		}
		BasicBSONList targets = (BasicBSONList) value;

		DBObject newElement = new BasicDBObject();
		newElement.put(SF_TARGET, target.get_id());
		newElement.put(SF_TARGET_CLASS, target.getClass().getName());
		newElement.put(SF_TARGET_EDITOR, editorId);
		newElement.put(SF_TARGET_EDITABLE, new Boolean(editable));
		if (targets.contains(newElement)) {
			return;
		}
		targets.add(newElement);
		setValue(F_TARGETS, targets);
	}
}
