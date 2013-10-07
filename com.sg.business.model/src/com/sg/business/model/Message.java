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
	private static final String F_SENDDATE = "senddate";

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
	 * 星标标志
	 */
	public static final String F_MARK_STAR = "markstar";

	/**
	 * 废件箱
	 */
	public static final String F_WASTE = "waste";

	/**
	 * 重要度
	 */
	public static final String F_IMPORTANCE = "importance";

	/**
	 * 电子邮件提醒发送日期
	 */
	public static final String F_EMAIL_NOTICE_DATE = "emailnoticedate";

	/**
	 * 附件,文件列表型字段
	 */
	public static final String F_ATTACHMENT = "attachment";

	/**
	 * 回复编辑器ID
	 */
	public static final String EDITOR_REPLY = "message.editor.reply";

	/**
	 * 新消息编辑器
	 */
	public static final String EDITOR_SEND = "message.editor.create";

	/**
	 * 消息查看编辑器
	 */
	public static final String EDITOR_VIEW = "message.editor.view";

	/**
	 * 消息查看HTML编辑器
	 */
	public static final String EDITOR_HTMLVIEW = "message.editor.htmlview";

	/**
	 * 使用html的内容
	 */
	public static final String F_ISHTMLBODY = "ishtmlcontent";

	/**
	 * 构建回复信息对象
	 * 
	 * @return Message
	 */
	public Message makeReply() {
		Message reply = ModelService.createModelObject(Message.class);
		reply.setValue(F_PARENT_MESSAGE, get_id());
		// 设置接收人
		Object value = getValue(F_SENDER);
		BasicDBList recieverList = new BasicDBList();
		recieverList.add(value);
		reply.setValue(F_RECIEVER, recieverList);
		reply.setValue(F_DESC, "RE:" + getDesc());
		return reply;
	}

	/**
	 * 设置消息是否已读
	 * 
	 * @param context
	 * @param isRead
	 *            ,是否已读
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
	 * 为消息添加星标
	 * 
	 * @param context
	 * @param isStar
	 *            ,是否添加星标
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
	 * 将消息添加到废件箱
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
	 * 判断是否已读
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
	 * 判断是否具有星标
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
	 * 判断是否加入到废件箱
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
	 * 返回回复图标路径
	 * 
	 * @return String
	 */
	public String getImageURLForReply() {
		return FileUtil.getImageURL(BusinessResource.IMAGE_MESSAGE_REPLY_24,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	}

	/**
	 * 返回已读消息图标路径
	 * 
	 * @return String
	 */
	public String getImageURLForOpen() {
		return FileUtil.getImageURL(BusinessResource.IMAGE_MESSAGE_24,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	}

	/**
	 * 返回未读消息图标路径
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
	 * 返回标题的显示内容
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

		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>");

		// 添加日期
		SimpleDateFormat sdf = new SimpleDateFormat(
				Utils.SDF_DATETIME_COMPACT_SASH);
		Date date = (Date) getValue(F_SENDDATE);
		String sendDate = sdf.format(date);
		sb.append("<span style='float:right;padding-right:4px'>");
		sb.append(sendDate);
		sb.append("</span>");

		// 添加图标
		String imageUrl = null;
		// 如果为已读消息，显示图标地址为getImageURLForOpen()
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
		// 添加主题
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
		 * BUG:10003 zhonghua 消息中显示发件人null, 这些发件人是系统发件或者是后台发件
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
	 * 返回接收人的显示内容
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
					// 如果收件人有多个人，则显示第一个收件人加省略号
					if (recieverList.size() > 1) {
						result += " ...";
					}
				}
			}
		}

		return result;
	}

	/**
	 * 判断是否为回复消息
	 * 
	 * @return boolean
	 */
	public boolean isReply() {
		return getValue(F_PARENT_MESSAGE) != null;
	}

	/**
	 * 插入发件人和发件日期
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

			// 使用自动刷新机制取代通知
			// 激活账户通知
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

			// // 激活账户通知
			// String[] recieverList = (String[]) value;
			// for (int i = 0; i < recieverList.length; i++) {
			// UserSessionContext.noticeAccountChanged(recieverList[i],
			// new AccountEvent(AccountEvent.EVENT_MESSAGE, this));
			// }
		} else {
			throw new Exception("缺少收件人");

		}

	}
	
	
	public static void main(String[] args) {
		BasicDBObject a = new BasicDBObject();
		a.append("id", new ObjectId());
		a.append("desc", "sdfsdkfjsdkjfjfkd中文g");
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
	 * 发送邮件通知
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
			sb.append("<br/><br/><br/>请查阅：");

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
	 * 向消息增加目标导航数据
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
		return "消息";
	}
}
