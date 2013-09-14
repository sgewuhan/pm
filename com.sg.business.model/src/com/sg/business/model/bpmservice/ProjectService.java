package com.sg.business.model.bpmservice;

import java.util.HashMap;
import java.util.Map;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;

public class ProjectService extends ServiceProvider {

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();

		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof IProjectRelative) {
				IProjectRelative lp = (IProjectRelative) host;
				Project project = lp.getProject();
				try {
					DBObject processData = WorkflowUtils
							.getProcessInfoFromJSON(jsonContent);
					String processId = (String) processData.get("processId");
					String processName = (String) processData
							.get("processName");
					if ("ready".equals(getOperation())) {
						project.doReady(new BPMServiceContext(processName,
								processId));
					} else if ("message".equals(getOperation())) {
						project.doCommitWithSendMessage(new BPMServiceContext(
								processName, processId));
					}
				} catch (Exception e) {
					result.put("returnCode", "ERROR");
					result.put("returnMessage", e.getMessage());
				}
			}
		}
		return result;
	}

}
