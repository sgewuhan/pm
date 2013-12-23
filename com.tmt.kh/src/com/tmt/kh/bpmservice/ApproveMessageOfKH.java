package com.tmt.kh.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.tmt.kh.nls.Messages;

public class ApproveMessageOfKH extends MessageService {

	public ApproveMessageOfKH() {
	}

	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		PrimaryObject host = getTarget();
		if ("documentapprove1".equals(messageOperation)) { //$NON-NLS-1$
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = Messages.get().ApproveMessageOfKH_1 + work.getLabel();
					String choice = (String) getInputValue("choice"); //$NON-NLS-1$
					if ("通过".equals(choice)) { //$NON-NLS-1$
						content = content + Messages.get().ApproveMessageOfKH_4;
					} else if ("不通过".equals(choice)) { //$NON-NLS-1$
						content = content + Messages.get().ApproveMessageOfKH_6;
					}
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		} else if ("documentapprove2".equals(messageOperation)) { //$NON-NLS-1$
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = Messages.get().ApproveMessageOfKH_8 + work.getLabel();
					String choice = (String) getInputValue("choice"); //$NON-NLS-1$
					if ("通过".equals(choice)) { //$NON-NLS-1$
						content = content + Messages.get().ApproveMessageOfKH_11;
					} else if ("不通过".equals(choice)) { //$NON-NLS-1$
						content = content + Messages.get().ApproveMessageOfKH_13;
					}
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		} else if ("documentapprove3".equals(messageOperation)) { //$NON-NLS-1$
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = Messages.get().ApproveMessageOfKH_15 + work.getLabel();
					String choice = (String) getInputValue("choice"); //$NON-NLS-1$
					if ("通过".equals(choice)) { //$NON-NLS-1$
						content = content + Messages.get().ApproveMessageOfKH_18;
					} else if ("不通过".equals(choice)) { //$NON-NLS-1$
						content = content + Messages.get().ApproveMessageOfKH_20;
					}
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		}else if ("documentApprove".equals(messageOperation)) { //$NON-NLS-1$
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = Messages.get().ApproveMessageOfKH_22 + work.getLabel();
					String choice = (String) getInputValue("choice"); //$NON-NLS-1$
					if ("通过".equals(choice)) { //$NON-NLS-1$
						content = content + Messages.get().ApproveMessageOfKH_25;
					} else if ("不通过".equals(choice)) { //$NON-NLS-1$
						content = content + Messages.get().ApproveMessageOfKH_27;
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
		if ("documentapprove1".equals(messageOperation)) { //$NON-NLS-1$
			if (host instanceof Work) {
				Work work = (Work) host;
				List<?> participatesIdList = work.getParticipatesIdList();
				return (List<String>) participatesIdList;
			}
		} else if ("documentapprove2".equals(messageOperation)) { //$NON-NLS-1$
			if (host instanceof Work) {
				Work work = (Work) host;
				List<?> participatesIdList = work.getParticipatesIdList();
				return (List<String>) participatesIdList;
			}
		} else if ("documentapprove3".equals(messageOperation)) { //$NON-NLS-1$
			if (host instanceof Work) {
				Work work = (Work) host;
				List<?> participatesIdList = work.getParticipatesIdList();
				return (List<String>) participatesIdList;
			}
		}else if ("documentApprove".equals(messageOperation)) { //$NON-NLS-1$
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
		if (Messages.get().ApproveMessageOfKH_32.equals(messageOperation)) {
			Object content = getInputValue("content"); //$NON-NLS-1$
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		} else if ("documentapprove2".equals(messageOperation)) { //$NON-NLS-1$
			Object content = getInputValue("content"); //$NON-NLS-1$
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		} else if ("documentapprove3".equals(messageOperation)) { //$NON-NLS-1$
			Object content = getInputValue("content"); //$NON-NLS-1$
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		}else if ("documentApprove".equals(messageOperation)) { //$NON-NLS-1$
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
		if ("documentapprove1".equals(messageOperation)) { //$NON-NLS-1$
			return Messages.get().ApproveMessageOfKH_41;
		} else if ("documentapprove2".equals(messageOperation)) { //$NON-NLS-1$
			return Messages.get().ApproveMessageOfKH_43;
		}else if ("documentapprove3".equals(messageOperation)) { //$NON-NLS-1$
			return Messages.get().ApproveMessageOfKH_45;
		}else if ("documentApprove".equals(messageOperation)) { //$NON-NLS-1$
			return Messages.get().ApproveMessageOfKH_47;
		}
		return super.getMessageTitle();
	}

	@Override
	public String getEditorId() {
		String messageOperation = getOperation();
		if ("documentapprove1".equals(messageOperation)) { //$NON-NLS-1$
			return Work.EDITOR;
		} else if ("documentapprove2".equals(messageOperation)) { //$NON-NLS-1$
			return Work.EDITOR;
		}else if ("documentapprove3".equals(messageOperation)) { //$NON-NLS-1$
			return Work.EDITOR;
		}else if ("documentApprove".equals(messageOperation)) { //$NON-NLS-1$
			return Work.EDITOR;
		}
		return super.getEditorId();
	}

}
