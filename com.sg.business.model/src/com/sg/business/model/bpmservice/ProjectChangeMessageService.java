package com.sg.business.model.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;

public class ProjectChangeMessageService extends MessageService {

	@Override
	public String getMessageTitle() {
		return "项目变更通知";
	}

	@Override
	public String getMessageContent() {
		Object choice = getInputValue("choice");
		if ("批准".equals((String) choice)) {
			return "项目" + getTarget().getLabel() + "：允许变更";
		} else {
			return "项目" + getTarget().getLabel() + "：不允许变更";
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
// messageTitle = "项目变更通知";
// Object choice = getInputValue("choice");
// messageContent = "项目" + project.getLabel();
// if ("批准".equals((String) choice)) {
// messageContent += "：允许变更";
// } else {
// messageContent += "：不允许变更";
// }
// } else if ("close".equals(getOperation())) {
// messageTitle = "项目结题通知";
// messageContent = "项目" + project.getLabel() + "：允许结题";
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

