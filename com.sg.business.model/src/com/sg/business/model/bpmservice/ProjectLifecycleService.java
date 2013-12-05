package com.sg.business.model.bpmservice;

import java.util.HashMap;
import java.util.Map;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.ILifecycle;

public class ProjectLifecycleService extends ServiceProvider {

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();

		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof ILifecycle) {
				ILifecycle lc = (ILifecycle) host;
				try {
					DBObject processData = WorkflowUtils
							.getProcessInfoFromJSON(jsonContent);
					String processId = (String) processData.get("processId");
					String processName = (String) processData
							.get("processName");
					
					if ("finish".equals(getOperation())) {
						lc.doFinish(new BPMServiceContext(processName, processId));
					} else if ("cancel".equals(getOperation())) {
						lc.doCancel(new BPMServiceContext(processName, processId));
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
