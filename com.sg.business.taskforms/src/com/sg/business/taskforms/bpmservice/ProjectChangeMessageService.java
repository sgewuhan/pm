package com.sg.business.taskforms.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;
import com.sg.business.model.bpmservice.MessageService;

public class ProjectChangeMessageService extends MessageService {

	@Override
	public String getMessageTitle() {
		return "��Ŀ���֪ͨ";
	}

	@Override
	public String getMessageContent() {
		Object choice = getInputValue("choice"); //$NON-NLS-1$
		if ("ͨ��".equals((String) choice)) {
			return "��Ŀ" + getTarget().getLabel() + "��������";
		} else {
			return "��Ŀ" + getTarget().getLabel() + "����������";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		Project pro = (Project) getTarget();
		if (pro != null) {
			return (List<String>) (pro.getParticipatesIdList());
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
			if (host instanceof IProjectRelative) {
				IProjectRelative lp = (IProjectRelative) host;
				Project project = lp.getProject();
				return project;
			}
		}
		return null;
	}
}



