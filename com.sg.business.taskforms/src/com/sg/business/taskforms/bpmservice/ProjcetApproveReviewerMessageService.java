package com.sg.business.taskforms.bpmservice;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;
import com.sg.business.model.bpmservice.MessageService;

public class ProjcetApproveReviewerMessageService extends MessageService {

	@Override
	public String getMessageTitle() {
		return "�������֪ͨ";
	}

	@Override
	public String getMessageContent() {
		Object choice = getInputValue("choice");
		if ("ͨ��".equals((String) choice)) {
			return "��Ŀ" + getTarget().getLabel() + "������ͨ��";
		} else {
			return "��Ŀ" + getTarget().getLabel() + "��������ͨ��";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		Object obj = getInputValue("reviewer_list");
		if (obj instanceof ArrayList) {
			return (List<String>)obj;
		} else
			return null;
	}

	@Override
	public String getEditorId() {
     	return Project.EDITOR_CREATE_PLAN;
	}

	@Override
	public PrimaryObject getTarget() {
		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof IProjectRelative) {
				IProjectRelative lp = (IProjectRelative) host;
				Project project = lp.getProject();
				return project;
			}
		}
		return null;
	}
}