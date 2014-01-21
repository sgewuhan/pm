package com.sg.business.taskforms.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.sg.business.resource.nls.Messages;

public class DocumentApprovalMessageService extends MessageService {

	public DocumentApprovalMessageService() {
	}

	@Override
	public String getMessageTitle() {
		// TODO Auto-generated method stub
		return Messages.get().DocumentApprovalMessageService_0;
	}

	@Override
	public String getMessageContent() {
		// TODO Auto-generated method stub
		  return Messages.get().DocumentApprovalMessageService_1 + getTarget().getLabel() + Messages.get().DocumentApprovalMessageService_2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		PrimaryObject target= getTarget();
		if(target instanceof Work )
		{
			Work work = (Work) target;
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
