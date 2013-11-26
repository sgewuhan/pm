package com.tmt.tb.bpmservice;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Project;
import com.sg.business.model.Work;

public class AddParticipateOfTB extends ServiceProvider {

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content");
		String _id = (String) getInputValue("project_id");
		String prj_financial = (String) getInputValue("act_prj_financial");
		if (_id == null) {
			result.put("returnCode", "ERROR");
			result.put("returnMessage", "无法添加财务到相应项目中");
		} else {
			if (content instanceof String) {
				String jsonContent = (String) content;
				try {
					ObjectId project_id = new ObjectId(_id);
					PrimaryObject host = WorkflowUtils
							.getHostFromJSON(jsonContent);
					if (host instanceof Work) {
						Project project = ModelService.createModelObject(
								Project.class, project_id);
						project.doAddParticipate(new String[] { prj_financial });
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
