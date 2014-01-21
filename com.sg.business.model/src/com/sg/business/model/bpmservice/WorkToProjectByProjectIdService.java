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
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.ProjectToolkit;
import com.sg.business.resource.nls.Messages;

public class WorkToProjectByProjectIdService extends ServiceProvider {

	public WorkToProjectByProjectIdService() {
	}

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content"); //$NON-NLS-1$
		if (content instanceof String) {
			String jsonContent = (String) content;
			try {
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				DBObject processData = WorkflowUtils
						.getProcessInfoFromJSON(jsonContent);
				String processId = (String) processData.get("processId"); //$NON-NLS-1$
				String processName = (String) processData.get("processName"); //$NON-NLS-1$
				if (host instanceof Work) {
					Work work = (Work) host;
					// 根据工作令号获取项目
					Project project = work.getProject();
					if (project != null) {
						return result;
					}
					// 根据项目id获取项目
					String projectid = (String) getInputValue("projectid"); //$NON-NLS-1$
					ObjectId project_id = new ObjectId(projectid);
					if (project_id != null) {
						project = ModelService.createModelObject(Project.class,
								project_id);
					}
					// 附加工作至项目中
					if (project != null) {
						ProjectToolkit
								.doProjectAddStandloneWork(work, project,
										(new BPMServiceContext(processName,
												processId)));
					}

				} else {
					result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
					result.put("returnMessage", Messages.get().WorkToProjectByProjectIdService_0); //$NON-NLS-1$
				}
			} catch (Exception e) {
				result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
				result.put("returnMessage", e.getMessage()); //$NON-NLS-1$
			}
		}
		return result;
	}

}
