package com.tmt.xt.bpmservice;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;

public class SupportServiceOfXT extends MessageService {

	@Override
	public String getMessageTitle() {
		return "技术支持通知";
	}

	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		if ("message".equals(messageOperation)) {
			Object choice = getInputValue("choice");
			if ("通过".equals((String) choice)) {
				return "技术支持工作" + getTarget().getLabel() + "：已完成";
			} else {
				return "技术支持工作" + getTarget().getLabel() + "：审批不通过";
			}
		} else if ("notice".equals(messageOperation)) {
			return "商务部发来一个技术支持工作:" + getTarget().getLabel();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		String messageOperation = getOperation();
		if ("message".equals(messageOperation)) {
			Work work = (Work) getTarget();
			if (work != null) {
				List<?> participatesIdList = work.getParticipatesIdList();
				return (List<String>) participatesIdList;
			}
		} else if ("notice".equals(messageOperation)) {
			List<String> supportnotice = (ArrayList<String>) getInputValue("supportnotice");
			return supportnotice;
		}
		return null;
	}

	@Override
	public PrimaryObject getTarget() {
		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof Work) {
				Work work = (Work) host;
				return work;
			}
		}
		return null;
	}
}
