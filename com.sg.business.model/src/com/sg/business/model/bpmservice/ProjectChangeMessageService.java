package com.sg.business.model.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;

public class ProjectChangeMessageService extends MessageService {

	@Override
	public String getMessageTitle() {
		return "��Ŀ���֪ͨ";
	}

	@Override
	public String getMessageContent() {
		Object choice = getInputValue("choice");
		if ("��׼".equals((String) choice)) {
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
//		return Project.EDITOR_CREATE_PLAN;
		return null;
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

// @Override
// public Map<String, Object> run(Object parameter) {
//
// HashMap<String, Object> result = new HashMap<String, Object>();
// Object content = getInputValue("content");
// if (content instanceof String) {
// String jsonContent = (String) content;
// PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
// if (host instanceof IProjectRelative) {
// IProjectRelative lp = (IProjectRelative) host;
// Project project = lp.getProject();
// try {
// DBObject processData = WorkflowUtils
// .getProcessInfoFromJSON(jsonContent);
// String processId = (String) processData.get("processId");
// String processName = (String) processData
// .get("processName");
//
// String messageTitle = "";
// String messageContent = "";
// if ("change".equals(getOperation())) {
// messageTitle = "��Ŀ���֪ͨ";
// Object choice = getInputValue("choice");
// messageContent = "��Ŀ" + project.getLabel();
// if ("��׼".equals((String) choice)) {
// messageContent += "��������";
// } else {
// messageContent += "����������";
// }
// } else if ("close".equals(getOperation())) {
// messageTitle = "��Ŀ����֪ͨ";
// messageContent = "��Ŀ" + project.getLabel() + "���������";
// }
// @SuppressWarnings("unchecked")
// List<String> receivers = (List<String>) project
// .getParticipatesIdList();
// sendMessage(messageTitle, messageContent, receivers,
// project, Project.EDITOR_CREATE_PLAN,
// new BPMServiceContext(processName, processId));
// } catch (Exception e) {
// result.put("returnCode", "ERROR");
// result.put("returnMessage", e.getMessage());
// }
// }
// }
// return result;
// }

