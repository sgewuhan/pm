package com.tmt.kfzx.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.tmt.kfzx.nls.Messages;

public class ApproveMessageOfKFZX extends MessageService {

	public ApproveMessageOfKFZX() {
	}

	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		PrimaryObject host = getTarget();
		if ("approvemessage".equals(messageOperation)) { //$NON-NLS-1$
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = Messages.get().ApproveMessageOfKFZX_1 + work.getLabel();
					String choice = (String) getInputValue("choice"); //$NON-NLS-1$
					if ("通过".equals(choice)) { //$NON-NLS-1$
						content = content + Messages.get().ApproveMessageOfKFZX_4;
					} else if ("不通过".equals(choice)) { //$NON-NLS-1$
						content = content + Messages.get().ApproveMessageOfKFZX_6;
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
		if ("approvemessage".equals(messageOperation)) { //$NON-NLS-1$
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
		if ("approvemessage".equals(messageOperation)) { //$NON-NLS-1$
			Object content = getInputValue("content"); //$NON-NLS-1$
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
		if ("approvemessage".equals(messageOperation)) { //$NON-NLS-1$
			return Messages.get().ApproveMessageOfKFZX_11;
		}
		return super.getMessageTitle();
	}

	@Override
	public String getEditorId() {
		String messageOperation = getOperation();
		if ("approvemessage".equals(messageOperation)) { //$NON-NLS-1$
			return Work.EDITOR;
		}
		return super.getEditorId();
	}

}
