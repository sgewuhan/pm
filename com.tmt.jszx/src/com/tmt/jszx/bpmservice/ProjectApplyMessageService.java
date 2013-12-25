package com.tmt.jszx.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.tmt.jszx.nls.Messages;

public class ProjectApplyMessageService extends MessageService {

	@Override
	public String getMessageTitle() {
		return Messages.get().ProjectApplyMessageService_0;
	}

	@Override
	public String getMessageContent() {
		Object choice = getInputValue("choice"); //$NON-NLS-1$
		if ("Í¨¹ý".equals((String) choice)) { //$NON-NLS-1$
			return Messages.get().ProjectApplyMessageService_3 + getTarget().getLabel() + Messages.get().ProjectApplyMessageService_4;
		} else {
			return Messages.get().ProjectApplyMessageService_5 + getTarget().getLabel() + Messages.get().ProjectApplyMessageService_6;
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
