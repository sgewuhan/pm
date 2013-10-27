package com.tmt.kh.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;

public class ApproveMessageOfKH extends MessageService {

	public ApproveMessageOfKH() {
	}

	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		PrimaryObject host = getTarget();
		if ("documentapprove1".equals(messageOperation)) {
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = "����:" + work.getLabel();
					String choice = (String) getInputValue("choice");
					if ("ͨ��".equals(choice)) {
						content = content + "����ͨ��!";
					} else if ("��ͨ��".equals(choice)) {
						content = content + "������ͨ��.";
					}
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		} else if ("documentapprove2".equals(messageOperation)) {
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = "����:" + work.getLabel();
					String choice = (String) getInputValue("choice");
					if ("ͨ��".equals(choice)) {
						content = content + "����ͨ��!";
					} else if ("��ͨ��".equals(choice)) {
						content = content + "������ͨ��.";
					}
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		} else if ("documentapprove3".equals(messageOperation)) {
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = "����:" + work.getLabel();
					String choice = (String) getInputValue("choice");
					if ("ͨ��".equals(choice)) {
						content = content + "����ͨ��!";
					} else if ("��ͨ��".equals(choice)) {
						content = content + "������ͨ��.";
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
		if ("documentapprove1".equals(messageOperation)) {
			if (host instanceof Work) {
				Work work = (Work) host;
				List<?> participatesIdList = work.getParticipatesIdList();
				return (List<String>) participatesIdList;
			}
		} else if ("documentapprove2".equals(messageOperation)) {
			if (host instanceof Work) {
				Work work = (Work) host;
				List<?> participatesIdList = work.getParticipatesIdList();
				return (List<String>) participatesIdList;
			}
		} else if ("documentapprove3".equals(messageOperation)) {
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
		if ("documentapprove1".equals(messageOperation)) {
			Object content = getInputValue("content");
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		} else if ("documentapprove2".equals(messageOperation)) {
			Object content = getInputValue("content");
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		} else if ("documentapprove3".equals(messageOperation)) {
			Object content = getInputValue("content");
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
		if ("documentapprove1".equals(messageOperation)) {
			return "�ĵ�����֪ͨ";
		} else if ("documentapprove2".equals(messageOperation)) {
			return "�ĵ�����֪ͨ";
		}else if ("documentapprove3".equals(messageOperation)) {
			return "�ĵ�����֪ͨ";
		}
		return super.getMessageTitle();
	}

	@Override
	public String getEditorId() {
		String messageOperation = getOperation();
		if ("documentapprove1".equals(messageOperation)) {
			return Work.EDITOR;
		} else if ("documentapprove2".equals(messageOperation)) {
			return Work.EDITOR;
		}else if ("documentapprove3".equals(messageOperation)) {
			return Work.EDITOR;
		}
		return super.getEditorId();
	}

}
