package com.tmt.jszx.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;

public class ProjectApplyMessageService extends MessageService {

	@Override
	public String getMessageTitle() {
		return "技术委托通知";
	}

	@Override
	public String getMessageContent() {
		Object choice = getInputValue("choice");
		if ("通过".equals((String) choice)) {
			return "工作" + getTarget().getLabel() + "：委托接受";
		} else {
			return "工作" + getTarget().getLabel() + "：委托否决";
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
