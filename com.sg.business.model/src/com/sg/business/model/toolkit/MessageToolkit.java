package com.sg.business.model.toolkit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.sg.business.model.Message;
import com.sg.business.model.Project;
import com.sg.business.model.Work;

public class MessageToolkit {

	public static void appendMessageContent(Message message, String contentLine) {
		Object value = message.getValue(Message.F_CONTENT);
		if (!(value instanceof String)) {
			message.setValue(Message.F_CONTENT, "<span style='font-size:14px'>"
					+ "����: " + "</span><br/><br/>"
					+ "���յ����ύ����Ŀ�ƻ���Ϣ��<br/>���ڼƻ��н�����Ͳ������¹�����<br/><br/>"
					+ contentLine);
		} else {
			message.setValue(Message.F_CONTENT, (String) value + "<br/>"
					+ contentLine);
		}
	}

	public static Message createProjectCommitMessage(String userId) {
		Message message = ModelService.createModelObject(Message.class);
		BasicDBList recievers = new BasicDBList();
		recievers.add(userId);
		message.setValue(Message.F_RECIEVER, recievers);
		message.setValue(Message.F_DESC, "��Ŀ�ƻ��ύ֪ͨ");
		message.setValue(Message.F_ISHTMLBODY, Boolean.TRUE);
		return message;
	}

	public static void appendWorkflowActorMessage(Work work,
			Map<String, Message> messageList, String processKey,
			String processName) {
		Message message;
		String userId;
		if (work.isWorkflowActivate(processKey)) {
			DBObject map = work.getProcessActorsMap(processKey);
			if (map != null) {
				Iterator<String> iter = map.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					userId = (String) map.get(key);
					message = messageList.get(userId);
					if (message == null) {
						message = MessageToolkit.createProjectCommitMessage(userId);
						messageList.put(userId, message);
					}
					MessageToolkit.appendMessageContent(message, "���빤�����̣�"
							+ processName + ": " + work.getLabel());
					message.appendTargets(work, Work.EDITOR, Boolean.TRUE);
				}
			}
		}
	}

	public static void appendWorkflowActorMessage(Project project,
			Map<String, Message> messageList, String processKey,
			String processName) {
		Message message;
		String userId;
		if (project.isWorkflowActivate(processKey)) {
			DBObject map = project.getProcessActorsMap(processKey);
			if (map != null) {
				Iterator<String> iter = map.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					userId = (String) map.get(key);
					message = messageList.get(userId);
					if (message == null) {
						message = MessageToolkit.createProjectCommitMessage(userId);
						messageList.put(userId, message);
					}
					MessageToolkit.appendMessageContent(message, "������Ŀ���̣�"
							+ processName + ": " + project.getLabel());
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
		message.setValue(Message.F_CONTENT, (String) value + "<br/>"
				+ "<br/><br/>"
						+ "������[����]�в鿴������Ϣ�Ķ�Ӧ����"
						+ "<br/>"
						+ "��������κ����ʣ��뼰ʱ�뷢���˹�ͨ����ֱ��[�ظ�]����Ϣ��"
						+ "<br/><br/>"
						+ "ף�������!"
						+ "<br/>"
						+ new SimpleDateFormat(Utils.SDF_DATE_WEEKDAY_TIME)
								.format(new Date()) + "<br/>"
						+ "������Ϣ��ϵͳ�Զ�������");
	}
}
