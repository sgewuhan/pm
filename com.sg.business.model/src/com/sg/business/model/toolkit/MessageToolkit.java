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
					+ "您好: " + "</span><br/><br/>"
					+ "您收到了提交的项目计划信息。<br/>您在计划中将负责和参与以下工作：<br/><br/>"
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
		message.setValue(Message.F_DESC, "项目计划提交通知");
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
					MessageToolkit.appendMessageContent(message, "参与工作流程，"
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
					MessageToolkit.appendMessageContent(message, "参与项目流程，"
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
						+ "您可在[导航]中查看以上消息的对应对象。"
						+ "<br/>"
						+ "如果您有任何疑问，请及时与发送人沟通，或直接[回复]本消息。"
						+ "<br/><br/>"
						+ "祝工作愉快!"
						+ "<br/>"
						+ new SimpleDateFormat(Utils.SDF_DATE_WEEKDAY_TIME)
								.format(new Date()) + "<br/>"
						+ "（本消息由系统自动代发）");
	}
}
