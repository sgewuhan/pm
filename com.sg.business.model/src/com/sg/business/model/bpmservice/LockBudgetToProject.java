package com.sg.business.model.bpmservice;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;
import com.sg.business.model.Work;

public class LockBudgetToProject extends ServiceProvider {

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content"); //$NON-NLS-1$
		String _id = (String) getInputValue("project_id"); //$NON-NLS-1$
		if (_id == null) {
			result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
			result.put("returnMessage", "此工作无法关联项目"); //$NON-NLS-1$
		} else {
			if (content instanceof String) {
				String jsonContent = (String) content;
				try {
					ObjectId project_id = new ObjectId(_id);
					DBObject processData = WorkflowUtils
							.getProcessInfoFromJSON(jsonContent);
					String processId = (String) processData.get("processId"); //$NON-NLS-1$
					String processName = (String) processData
							.get("processName"); //$NON-NLS-1$

					PrimaryObject host = WorkflowUtils
							.getHostFromJSON(jsonContent);
					if (host instanceof Work) {
						Project project = ModelService.createModelObject(
								Project.class, project_id);
						ProjectBudget projectBudget = project.getBudget();
						projectBudget.doLock(new BPMServiceContext(processName, processId));
					} else {
						result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
						result.put("returnMessage", "此工作无法发起项目"); //$NON-NLS-1$
					}
				} catch (Exception e) {
					result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
					result.put("returnMessage", e.getMessage()); //$NON-NLS-1$
				}
			}
		}
		return result;
	}

}
