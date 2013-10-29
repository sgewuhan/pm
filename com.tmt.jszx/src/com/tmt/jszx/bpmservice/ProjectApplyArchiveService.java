package com.tmt.jszx.bpmservice;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.Organization;
import com.sg.business.model.Work;

public class ProjectApplyArchiveService extends ServiceProvider {

	public ProjectApplyArchiveService() {
	}

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			try {
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					Work work = (Work) host;
					ObjectId orgId = null;
					IProcessControl ip = work.getAdapter(IProcessControl.class);
					BasicBSONList historys = ip.getWorkflowHistroyData(
							IWorkCloneFields.F_WF_EXECUTE, true);
					for (int i = 0; i < historys.size(); i++) {
						DBObject history = (DBObject) historys.get(i);
						String taskname = (String) history
								.get(IProcessControl.F_WF_TASK_NAME);
						if ("申请技术支持".equals(taskname)) {
							Object object = history.get("dept");
							if (object instanceof ObjectId) {
								orgId = (ObjectId) object;
								continue;
							}
						}
						if (orgId != null) {
							Organization org = ModelService.createModelObject(
									Organization.class, orgId);
							ObjectId containerOrgId = org
									.getContainerOrganizationId();
							if (containerOrgId != null) {
								String filderNmae = ""
										+ work.getProject().getDesc();
								filderNmae = filderNmae + "-" + work.getDesc();
								ObjectId folderId;
								
							} else {
								result.put("returnCode", "ERROR");
								result.put("returnMessage", "委托组织无法进行文件归档!");
							}
						}
					}
				}
			} catch (Exception e) {
				result.put("returnCode", "ERROR");
				result.put("returnMessage", e.getMessage());
			}
		}
		return result;
	}

}
