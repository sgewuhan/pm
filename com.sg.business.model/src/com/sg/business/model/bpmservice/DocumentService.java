package com.sg.business.model.bpmservice;

import java.util.HashMap;
import java.util.Map;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;

public class DocumentService extends ServiceProvider {

	@Override
	public Map<String, Object> run(Object arg0) {
		HashMap<String, Object> result = new HashMap<String, Object>();

		Object content = getInputValue("content"); //$NON-NLS-1$
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof Work) {
				Work work = (Work) host;
				DBObject processData = WorkflowUtils
						.getProcessInfoFromJSON(jsonContent);
				String processId = (String) processData
						.get("processId"); //$NON-NLS-1$
				String processName = (String) processData
						.get("processName"); //$NON-NLS-1$
				work.doSetDocumentLock(new BPMServiceContext(
						processName, processId),"lock".equals(getOperation())); //$NON-NLS-1$
			}
		}
		return result;
	}

}
