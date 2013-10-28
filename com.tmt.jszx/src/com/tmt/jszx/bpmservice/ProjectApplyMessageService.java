package com.tmt.jszx.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;

public class ProjectApplyMessageService extends MessageService {

	@Override
	public String getMessageTitle() {
		return "����ί��֪ͨ";
	}

	@Override
	public String getMessageContent() {
		Object choice = getInputValue("choice");
		if ("ͨ��".equals((String) choice)) {
			return "����" + getTarget().getLabel() + "��ί�н���";
		} else {
			return "����" + getTarget().getLabel() + "��ί�з��";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		Work work = (Work) getTarget();
		if (work != null) {
			List<?> participatesIdList = work.getParticipatesIdList();
			return (List<String>) participatesIdList;
		} else
			return null;
	}

	@Override
	public String getEditorId() {
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
