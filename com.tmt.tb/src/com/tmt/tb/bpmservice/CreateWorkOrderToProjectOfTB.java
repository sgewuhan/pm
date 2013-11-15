package com.tmt.tb.bpmservice;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.BPMServiceContext;

public class CreateWorkOrderToProjectOfTB extends ServiceProvider {

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content");
		String _id = (String) getInputValue("project_id");
		String workOrder = (String) getInputValue("prj_number");
		if (_id == null) {
			result.put("returnCode", "ERROR");
			result.put("returnMessage", "此工作无法关联项目");
		} else {
			if (content instanceof String) {
				String jsonContent = (String) content;
				try {
					ObjectId project_id = new ObjectId(_id);
					DBObject processData = WorkflowUtils
							.getProcessInfoFromJSON(jsonContent);
					String processId = (String) processData.get("processId");
					String processName = (String) processData
							.get("processName");

					PrimaryObject host = WorkflowUtils
							.getHostFromJSON(jsonContent);
					if (host instanceof Work) {
						Project project = ModelService.createModelObject(
								Project.class, project_id);
						project.setValue(Project.F_WORK_ORDER,
								new String[] { workOrder });
						project.doSave(new BPMServiceContext(processName,
								processId));
					} else {
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "此工作无法发起项目");
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
