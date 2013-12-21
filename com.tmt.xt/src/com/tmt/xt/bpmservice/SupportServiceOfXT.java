package com.tmt.xt.bpmservice;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.tmt.xt.nls.Messages;

public class SupportServiceOfXT extends MessageService {

	@Override
	public String getMessageTitle() {
		return Messages.get().SupportServiceOfXT_0;
	}

	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		if ("message".equals(messageOperation)) { //$NON-NLS-1$
			Object choice = getInputValue("choice"); //$NON-NLS-1$
			if ("Í¨¹ý".equals((String) choice)) { //$NON-NLS-1$
				return Messages.get().SupportServiceOfXT_4 + getTarget().getLabel() + Messages.get().SupportServiceOfXT_5;
			} else {
				return Messages.get().SupportServiceOfXT_6 + getTarget().getLabel() + Messages.get().SupportServiceOfXT_7;
			}
		} else if ("notice".equals(messageOperation)) { //$NON-NLS-1$
			return Messages.get().SupportServiceOfXT_9 + getTarget().getLabel();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		String messageOperation = getOperation();
		if ("message".equals(messageOperation)) { //$NON-NLS-1$
			Work work = (Work) getTarget();
			if (work != null) {
				List<?> participatesIdList = work.getParticipatesIdList();
				return (List<String>) participatesIdList;
			}
		} else if ("notice".equals(messageOperation)) { //$NON-NLS-1$
			List<String> supportnotice = (ArrayList<String>) getInputValue("supportnotice"); //$NON-NLS-1$
			return supportnotice;
		}
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
