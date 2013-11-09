package com.sg.business.model.bpmservice;

import java.util.HashMap;
import java.util.Map;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;

public class DeliverableService extends ServiceProvider {

	@Override
	public Map<String, Object> run(Object arg0) {
		HashMap<String, Object> result = new HashMap<String, Object>();

		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof Work) {
				Work work = (Work) host;
				DBObject processData = WorkflowUtils
						.getProcessInfoFromJSON(jsonContent);
				String processId = (String) processData.get("processId");
				String processName = (String) processData.get("processName");
				work.doChangeDeliverableLifeCycleStatus(new BPMServiceContext(
						processName, processId), getOperation());
				
			}
		}
		return result;
	}

}
