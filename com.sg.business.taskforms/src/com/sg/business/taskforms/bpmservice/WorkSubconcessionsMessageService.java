package com.sg.business.taskforms.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.sg.business.taskforms.nls.Messages;

public class WorkSubconcessionsMessageService extends MessageService {

	@Override
	public String getMessageTitle() {
		return Messages.get().WorkSubconcessionsMessageService_0;
	}

	@Override
	public String getMessageContent() {
		Object choice = getInputValue("choice"); //$NON-NLS-1$
		if ("通过".equals((String) choice)) { //$NON-NLS-1$
			return Messages.get().WorkSubconcessionsMessageService_3 + getTarget().getLabel() + Messages.get().WorkSubconcessionsMessageService_4;
		} else {
			return "工作" + getTarget().getLabel() + Messages.get().WorkSubconcessionsMessageService_6; //$NON-NLS-1$
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

