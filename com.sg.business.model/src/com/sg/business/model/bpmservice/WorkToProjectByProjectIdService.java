package com.sg.business.model.bpmservice;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.ProjectToolkit;

public class WorkToProjectByProjectIdService extends ServiceProvider {

	public WorkToProjectByProjectIdService() {
	}

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			try {
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				String processId = (String) host.getValue("processId");
				String processName = (String) host.getValue("processName");
				if (host instanceof Work) {
					Work work = (Work) host;
					// ���ݹ�����Ż�ȡ��Ŀ
					Project project = work.getProject();
					if (project != null) {
						return result;
					}
					// ������Ŀid��ȡ��Ŀ
					String projectid = (String) getInputValue("projectid");
					ObjectId project_id = new ObjectId(projectid);
					if (project_id != null) {
						project = ModelService.createModelObject(Project.class,
								project_id);
					}
					// ���ӹ�������Ŀ��
					if (project != null) {
						ProjectToolkit
								.doProjectAddStandloneWork(work, project,
										(new BPMServiceContext(processName,
												processId)));
					}

				} else {
					result.put("returnCode", "ERROR");
					result.put("returnMessage", "���ӹ������ִ���!");
				}
			} catch (Exception e) {
				result.put("returnCode", "ERROR");
				result.put("returnMessage", e.getMessage());
			}
		}
		return result;
	}

}
