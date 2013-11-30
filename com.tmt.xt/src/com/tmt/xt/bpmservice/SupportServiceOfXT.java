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
		return "����֧��֪ͨ";
	}

	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		if ("message".equals(messageOperation)) {
			Object choice = getInputValue("choice");
			if ("ͨ��".equals((String) choice)) {
				return "����֧�ֹ���" + getTarget().getLabel() + "�������";
			} else {
				return "����֧�ֹ���" + getTarget().getLabel() + "��������ͨ��";
			}
		} else if ("notice".equals(messageOperation)) {
			return "���񲿷���һ������֧�ֹ���:" + getTarget().getLabel();
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
