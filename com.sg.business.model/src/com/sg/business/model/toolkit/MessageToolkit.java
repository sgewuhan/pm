package com.sg.business.model.toolkit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.Message;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.nls.Messages;

public class MessageToolkit {

	public static void appendCommitMessageContent(Message message, String contentLine) {
		Object value = message.getValue(Message.F_CONTENT);
		if (!(value instanceof String)) {
			message.setValue(Message.F_CONTENT, "<span style='font-size:14px'>" //$NON-NLS-1$
					+ Messages.get().MessageToolkit_1 + "</span><br/><br/>" //$NON-NLS-2$
					+ Messages.get().MessageToolkit_3
					+ contentLine);
		} else {
			message.setValue(Message.F_CONTENT, (String) value + "<br/>" //$NON-NLS-1$
					+ contentLine);
		}
	}

	public static Message makeMessage(String userId, String title,String senderId,String content) {
		BasicDBList recievers = new BasicDBList();
		recievers.add(userId);
		return makeMessage(recievers,title,senderId,content);
	}
	
	public static Message makeMessage(List<?> recievers, String title,String senderId,String content) {
		Message message = ModelService.createModelObject(Message.class);
		message.setValue(Message.F_RECIEVER, recievers);
		message.setValue(Message.F_DESC, title);
		message.setValue(Message.F_ISHTMLBODY, Boolean.TRUE);
		message.setValue(Message.F_SENDER, senderId);
		message.setValue(Message.F_CONTENT, content);
		return message;
	}

	public static void appendWorkflowActorMessage(Work work,
			Map<String, Message> messageList, String processKey,
			String processName, String title,String senderId,String content) {
		Message message;
		String userId;
		IProcessControl pc = (IProcessControl) work.getAdapter(IProcessControl.class);

		if (pc.isWorkflowActivate(processKey)) {
			DBObject map = pc.getProcessActorsData(processKey);
			if (map != null) {
				Iterator<String> iter = map.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					userId = (String) map.get(key);
					message = messageList.get(userId);
					if (message == null) {
						message = makeMessage(userId,title,senderId,content);
						messageList.put(userId, message);
					}
					MessageToolkit.appendCommitMessageContent(message, Messages.get().MessageToolkit_5
							+ processName + ": " + work.getLabel()); //$NON-NLS-1$
					message.appendTargets(work, Work.EDITOR, Boolean.TRUE);
				}
			}
		}
	}

	public static void appendWorkflowActorMessage(Project project,
			Map<String, Message> messageList, String processKey,
			String processName, String title,String senderId,String content) {
		Message message;
		String userId;
		IProcessControl pc = (IProcessControl) project.getAdapter(IProcessControl.class);
		if (pc.isWorkflowActivate(processKey)) {
			DBObject map = pc.getProcessActorsData(processKey);
			if (map != null) {
				Iterator<String> iter = map.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					userId = (String) map.get(key);
					message = messageList.get(userId);
					if (message == null) {
						message = makeMessage(userId,title,senderId,content);
						messageList.put(userId, message);
					}
					MessageToolkit.appendCommitMessageContent(message, Messages.get().MessageToolkit_7
							+ processName + ": " + project.getLabel()); //$NON-NLS-1$
					message.appendTargets(project, Work.EDITOR, Boolean.TRUE);
				}
			}
		}
	}

	public static void appendProjectCommitMessageEndContent(
			Map<String, Message> messageList) {
		Iterator<Message> iter = messageList.values().iterator();
		while (iter.hasNext()) {
			Message message = iter.next();
			
			appendEndMessage(message);
		}
	}

	public static void appendEndMessage(Message message) {
		Object value = message.getValue(Message.F_CONTENT);
		message.setValue(Message.F_CONTENT, (String) value + "<br/>" //$NON-NLS-1$
				+ "<br/><br/>" //$NON-NLS-1$
						+ Messages.get().MessageToolkit_11
						+ "<br/>" //$NON-NLS-1$
						+ Messages.get().MessageToolkit_13
						+ "<br/><br/>" //$NON-NLS-1$
						+ Messages.get().MessageToolkit_15
						+ "<br/>" //$NON-NLS-1$
						+ new SimpleDateFormat(Utils.SDF_DATE_WEEKDAY_TIME)
								.format(new Date()) + "<br/>" //$NON-NLS-1$
						+ Messages.get().MessageToolkit_18);
	}

	public static void appendMessage(Map<String, Message> messageList,
			String receiverId, String title, String content,
			PrimaryObject target, String editId, IContext context) {
		Message message;
		if (receiverId == null) {
			return;
		}
		message = messageList.get(receiverId);
		if (message == null) {
			message = MessageToolkit.makeMessage(receiverId, title, context
					.getAccountInfo().getConsignerId(), null);
			messageList.put(receiverId, message);
		}
		MessageToolkit.appendCommitMessageContent(message, content);
		message.appendTargets(target, editId, Boolean.TRUE);
		messageList.put(receiverId, message);
	}
	public static void appendNoCommitMessage(Map<String, Message> messageList,
			String receiverId, String title, String content,
			PrimaryObject target, String editId, IContext context) {
		Message message;
		if (receiverId == null) {
			return;
		}
		message = messageList.get(receiverId);
		if (message == null) {
			message = MessageToolkit.makeMessage(receiverId, title, context
					.getAccountInfo().getConsignerId(), null);
			messageList.put(receiverId, message);
		}
		message.setValue(Message.F_CONTENT, content);
		message.appendTargets(target, editId, Boolean.TRUE);
		messageList.put(receiverId, message);
	}
}
