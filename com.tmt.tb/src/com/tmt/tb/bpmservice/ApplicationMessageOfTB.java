package com.tmt.tb.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;

public class ApplicationMessageOfTB extends MessageService {

	public ApplicationMessageOfTB() {
	}

	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		PrimaryObject host = getTarget();
		if ("applicationcancelmessage".equals(messageOperation)) {
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = "项目立项工作:" + work.getLabel();
					content = content + "审批不通过.";
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		} else if ("applicationfinishmessage".equals(messageOperation)) {
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = "项目立项工作:" + work.getLabel();
					content = content + "审批通过.";
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		}
		return super.getMessageContent();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		String messageOperation = getOperation();
		PrimaryObject host = getTarget();
		if ("applicationcancelmessage".equals(messageOperation)) {
			if (host instanceof Work) {
				Work work = (Work) host;
				List<?> participatesIdList = work.getParticipatesIdList();
				return (List<String>) participatesIdList;
			}
		} else if ("applicationfinishmessage".equals(messageOperation)) {
			if (host instanceof Work) {
				Work work = (Work) host;
				List<?> participatesIdList = work.getParticipatesIdList();
				return (List<String>) participatesIdList;
			}
		}
		return super.getReceiverList();
	}

	@Override
	public PrimaryObject getTarget() {
		String messageOperation = getOperation();
		if ("applicationcancelmessage".equals(messageOperation)) {
			Object content = getInputValue("content");
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		} else if ("applicationfinishmessage".equals(messageOperation)) {
			Object content = getInputValue("content");
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		}
		return super.getTarget();
	}

	@Override
	public String getMessageTitle() {
		String messageOperation = getOperation();
		if ("applicationcancelmessage".equals(messageOperation)) {
			return "项目立项审批通知";
		} else if ("applicationfinishmessage".equals(messageOperation)) {
			return "项目立项审批通知";
		}
		return super.getMessageTitle();
	}

}
