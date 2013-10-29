package com.tmt.kfzx.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;

public class ApproveMessageOfKFZX extends MessageService {

	public ApproveMessageOfKFZX() {
	}

	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		PrimaryObject host = getTarget();
		if ("approvemessage".equals(messageOperation)) {
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = "工作:" + work.getLabel();
					String choice = (String) getInputValue("choice");
					if ("通过".equals(choice)) {
						content = content + "审批通过!";
					} else if ("不通过".equals(choice)) {
						content = content + "审批不通过.";
					}
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
		if ("approvemessage".equals(messageOperation)) {
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
		if ("approvemessage".equals(messageOperation)) {
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
		if ("approvemessage".equals(messageOperation)) {
			return "图纸、文件审批通知";
		}
		return super.getMessageTitle();
	}

	@Override
	public String getEditorId() {
		String messageOperation = getOperation();
		if ("approvemessage".equals(messageOperation)) {
			return Work.EDITOR;
		}
		return super.getEditorId();
	}

}
