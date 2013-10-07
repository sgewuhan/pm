package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.codec.Coder;
import com.mobnut.commons.email.MailJob;
import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.Portal;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.CurrentAccountContext;

public class Message extends PrimaryObject implements IReferenceContainer {

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
	private static final String F_SENDDATE = "senddate";

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
	 * �Ǳ��־
	 */
	public static final String F_MARK_STAR = "markstar";

	/**
	 * �ϼ���
	 */
	public static final String F_WASTE = "waste";

	/**
	 * ��Ҫ��
	 */
	public static final String F_IMPORTANCE = "importance";

	/**
	 * �����ʼ����ѷ�������
	 */
	public static final String F_EMAIL_NOTICE_DATE = "emailnoticedate";

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
	 * ��Ϣ�鿴HTML�༭��
	 */
	public static final String EDITOR_HTMLVIEW = "message.editor.htmlview";

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
	 * ������Ϣ�Ƿ��Ѷ�
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
		// setValue(F_RECIEVEDDATE, new Date());
		doSave(context);
	}

	/**
	 * Ϊ��Ϣ����Ǳ�
	 * 
	 * @param context
	 * @param isStar
	 *            ,�Ƿ�����Ǳ�
	 * @throws Exception
	 */
	public void doMarkStar(IContext context, Boolean isStar) throws Exception {
		Object markStarData = getValue(F_MARK_STAR);
		if (!(markStarData instanceof DBObject)) {
			markStarData = new BasicDBObject();
		}
		((DBObject) markStarData).put(context.getAccountInfo().getUserId(),
				isStar);
		setValue(F_MARK_STAR, markStarData);
		doSave(context);
	}

	/**
	 * ����Ϣ��ӵ��ϼ���
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		Object wasteData = getValue(F_WASTE);
		if (!(wasteData instanceof DBObject)) {
			wasteData = new BasicDBObject();
		}
		((DBObject) wasteData).put(context.getAccountInfo().getUserId(),
				Boolean.TRUE);
		setValue(F_WASTE, wasteData);
		doSave(context);
	}

	public void doRestore(IContext context, Boolean isRestore) throws Exception {
		Object wasteData = getValue(F_WASTE);
		if (!(wasteData instanceof DBObject)) {
			wasteData = new BasicDBObject();
		}
		((DBObject) wasteData).put(context.getAccountInfo().getUserId(),
				isRestore);
		setValue(F_WASTE, wasteData);
		doSave(context);
	}

	/**
	 * �ж��Ƿ��Ѷ�
	 * 
	 * @param context
	 * @return boolean
	 */
	public boolean isRead(IContext context) {
		Object markReadData = getValue(F_MARK_READ);
		if (!(markReadData instanceof DBObject)) {
			return false;
		}
		String userId = context.getAccountInfo().getUserId();
		return Boolean.TRUE.equals(((DBObject) markReadData).get(userId));
	}

	/**
	 * �ж��Ƿ�����Ǳ�
	 * 
	 * @param context
	 * @return boolean
	 */
	public boolean isStar(IContext context) {
		Object markStarData = getValue(F_MARK_STAR);
		if (!(markStarData instanceof DBObject)) {
			return false;
		}
		String userId = context.getAccountInfo().getUserId();
		return Boolean.TRUE.equals(((DBObject) markStarData).get(userId));
	}

	/**
	 * �ж��Ƿ���뵽�ϼ���
	 * 
	 * @param context
	 * @return boolean
	 */
	public boolean isWaste(IContext context) {
		Object wasteData = getValue(F_WASTE);
		if (!(wasteData instanceof DBObject)) {
			return false;
		}
		String userId = context.getAccountInfo().getUserId();
		return Boolean.TRUE.equals(((DBObject) wasteData).get(userId));
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
		return FileUtil.getImageURL(BusinessResource.IMAGE_MESSAGE_24,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	}

	/**
	 * ����δ����Ϣͼ��·��
	 * 
	 * @return String
	 */
	public String getImageURL() {
		return FileUtil.getImageURL(BusinessResource.IMAGE_MESSAGE_UNREAD_24,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	}

	public String getImageURLForStar() {
		return FileUtil.getImageURL(BusinessResource.IMAGE_STAR_14,
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
		boolean isStar = isStar(new CurrentAccountContext());
		String userid = "";
		try {
			userid = UserSessionContext.getAccountInfo().getConsignerId();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		StringBuffer sb = new StringBuffer();

		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:9pt'>");

		// �������
		SimpleDateFormat sdf = new SimpleDateFormat(
				Utils.SDF_DATETIME_COMPACT_SASH);
		Date date = (Date) getValue(F_SENDDATE);
		String sendDate = sdf.format(date);
		sb.append("<span style='float:right;padding-right:4px'>");
		sb.append(sendDate);
		sb.append("</span>");

		// ���ͼ��
		String imageUrl = null;
		// ���Ϊ�Ѷ���Ϣ����ʾͼ���ַΪgetImageURLForOpen()
		if (isRead || userid.equals(getValue(F_SENDER))) {
			imageUrl = "<img src='"
					+ getImageURLForOpen()
					+ "' style='float:left;padding:6px' width='24' height='24' />";
		} else {
			imageUrl = "<img src='"
					+ (isReply ? getImageURLForReply() : getImageURL())
					+ "' style='float:left;padding:6px' width='24' height='24' />";
		}
		sb.append(imageUrl);

		if (isStar) {
			sb.append("<img src='" + getImageURLForStar()
					+ "' width='14' height='14' />");
		}
		// �������
		String label = getLabel();
		label = Utils.getPlainText(label);
		label = Utils.getLimitLengthString(label, 40);
		if (isRead || getValue(F_SENDER).equals(userid)) {
			sb.append(label);
		} else {
			sb.append("<b>" + label + "</b>");
		}
		Object importance = getValue(F_IMPORTANCE);
		if (!Utils.isNullOrEmptyString(importance)) {
			sb.append("  (");
			sb.append(importance);
			sb.append(")");
		}

		sb.append("</span><br/>");

		sb.append("<small>");

		String senderId = (String) getValue(F_SENDER);
		User sender = UserToolkit.getUserById(senderId);
		/**
		 * BUG:10003 zhonghua ��Ϣ����ʾ������null, ��Щ��������ϵͳ���������Ǻ�̨����
		 */
		if (sender == null) {
			sb.append("From: " + senderId);
		} else {
			sb.append("From: " + sender);
		}
		sb.append("  ");
		String recieverLabel = getRecieverLabel();
		sb.append("To: " + recieverLabel);

		sb.append("</small>");

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
				User user = UserToolkit.getUserById(userId);
				if (user == null) {
					return userId;
				} else {
					result = user.getLabel();
					// ����ռ����ж���ˣ�����ʾ��һ���ռ��˼�ʡ�Ժ�
					if (recieverList.size() > 1) {
						result += " ...";
					}
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

		String sender = (String) getValue(F_SENDER);
		if (sender == null) {
			sender = context.getAccountInfo().getUserId();
		}
		if (value instanceof BasicBSONList) {
			setValue(F_SENDDATE, new Date());
			setValue(F_SENDER, sender);

			sendEmailNotice();
			super.doInsert(context);

			// ʹ���Զ�ˢ�»���ȡ��֪ͨ
			// �����˻�֪ͨ
			// BasicBSONList recieverList = (BasicBSONList) value;
			// for (int i = 0; i < recieverList.size(); i++) {
			// UserSessionContext.noticeAccountChanged((String) recieverList
			// .get(i), new AccountEvent(AccountEvent.EVENT_MESSAGE,
			// this));
			// }

		} else if (value instanceof String[]) {
			setValue(F_SENDDATE, new Date());
			setValue(F_SENDER, sender);

			sendEmailNotice();
			super.doInsert(context);

			// // �����˻�֪ͨ
			// String[] recieverList = (String[]) value;
			// for (int i = 0; i < recieverList.length; i++) {
			// UserSessionContext.noticeAccountChanged(recieverList[i],
			// new AccountEvent(AccountEvent.EVENT_MESSAGE, this));
			// }
		} else {
			throw new Exception("ȱ���ռ���");

		}

	}
	
	
	public static void main(String[] args) {
		BasicDBObject a = new BasicDBObject();
		a.append("id", new ObjectId());
		a.append("desc", "sdfsdkfjsdkjfjfkd����g");
		String string = a.toString();
		
		try {
			String b = null;
			b = Coder.encryptBASE64(string.getBytes());
			System.out.println(b);

			byte[] c = Coder.decryptBASE64(b);
			System.out.println(new String(c));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * �����ʼ�֪ͨ
	 */
	private void sendEmailNotice() {

		Object value = getValue(F_RECIEVER);
		Set<User> recieverList = new HashSet<User>();
		if (value instanceof List<?>) {
			List<?> list = (List<?>) value;
			for (int i = 0; i < list.size(); i++) {
				String userId = (String) list.get(i);
				User user = UserToolkit.getUserById(userId);
				if (user != null) {
					recieverList.add(user);
				}
			}
		} else if (value instanceof String[]) {
			String[] list = (String[]) value;
			for (int i = 0; i < list.length; i++) {
				String userId = (String) list[i];
				User user = UserToolkit.getUserById(userId);
				if (user != null) {
					recieverList.add(user);
				}
			}
		}

		String subject = getDesc();

		StringBuffer sb = new StringBuffer();
		sb.append(getContent());

		BasicBSONList targetlist = getTargetList();
		if (targetlist != null && targetlist.size() > 0) {
			sb.append("<br/><br/><br/>����ģ�");

			for (int i = 0; i < targetlist.size(); i++) {
				sb.append("<br/><a href='"+ Portal.getHttpRoot()+ "/direct?");
				DBObject target = (DBObject) targetlist.get(i);
				sb.append("id="+target.get(SF_TARGET));
				sb.append("&class="+target.get(SF_TARGET_CLASS));
				sb.append("&editable="+target.get(SF_TARGET_EDITABLE));
				sb.append("&edittype="+target.get(SF_TARGET_EDITING_TYPE));
				sb.append("&editor="+target.get(SF_TARGET_EDITOR));
				sb.append("'>" + target.get(SF_TARGET_NAME) + "</a>");

			}
		}

		Iterator<User> iter = recieverList.iterator();
		while (iter.hasNext()) {
			User reciever = iter.next();
			String to = reciever.getEmail();
			String content = reciever.getUsername() + ", " + sb.toString();
			MailJob job = new MailJob(to, subject, content);
			job.schedule();
		}
	}

	public String getContent() {
		return (String) getValue(F_CONTENT);
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
		newElement.put(SF_TARGET_NAME, target.getDesc());
		newElement.put(SF_TARGET_CLASS, target.getClass().getName());
		newElement.put(SF_TARGET_EDITOR, editorId);
		newElement.put(SF_TARGET_EDITABLE, new Boolean(editable));
		if (targets.contains(newElement)) {
			return;
		}
		targets.add(newElement);
		setValue(F_TARGETS, targets);
	}

	public BasicBSONList getTargetList() {
		return (BasicBSONList) getValue(F_TARGETS);
	}

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_MESSAGE_16);
	}

	@Override
	public String getTypeName() {
		return "��Ϣ";
	}
}
