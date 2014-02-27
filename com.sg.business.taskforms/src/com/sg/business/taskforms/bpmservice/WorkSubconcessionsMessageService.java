package com.sg.business.taskforms.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;

public class WorkSubconcessionsMessageService extends MessageService {

	@Override
	public String getMessageTitle() {
		return "��������֪ͨ";
	}

	@Override
	public String getMessageContent() {
		Object choice = getInputValue("choice"); //$NON-NLS-1$
		if ("ͨ��".equals((String) choice)) { //$NON-NLS-1$
			return "����" + getTarget().getLabel() + "������ͨ��";
		} else {
			return "����" + getTarget().getLabel() + "��������ͨ��"; 
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
	public PrimaryObject getTarget() {
		Object content = getInputValue("content"); //$NON-NLS-1$
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

