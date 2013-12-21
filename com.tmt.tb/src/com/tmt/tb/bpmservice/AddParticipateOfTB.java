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
import com.tmt.tb.nls.Messages;

public class AddParticipateOfTB extends ServiceProvider {

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content"); //$NON-NLS-1$
		String _id = (String) getInputValue("project_id"); //$NON-NLS-1$
		String prj_financial = (String) getInputValue("act_prj_financial"); //$NON-NLS-1$
		if (_id == null) {
			result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
			result.put("returnMessage", Messages.get().AddParticipateOfTB_6); //$NON-NLS-1$
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
						result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
						result.put("returnMessage", Messages.get().AddParticipateOfTB_10); //$NON-NLS-1$
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
