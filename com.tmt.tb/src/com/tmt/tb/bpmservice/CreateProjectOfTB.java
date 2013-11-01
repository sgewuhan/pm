package com.tmt.tb.bpmservice;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Organization;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.BPMServiceContext;
import com.sg.business.model.toolkit.ProjectToolkit;

public class CreateProjectOfTB extends ServiceProvider {

	public CreateProjectOfTB() {
	}

	@Override
	public Map<String, Object> run(Object parameter) {

		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			try {
				DBObject processData = WorkflowUtils
						.getProcessInfoFromJSON(jsonContent);
				String processId = (String) processData.get("processId");
				String processName = (String) processData.get("processName");

				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					Work work = (Work) host;
					ObjectId launchorg_id = null;
					ObjectId org_id = null;
					String dept = (String) getInputValue("dept");
					if (dept != null) {
						launchorg_id = new ObjectId(dept);
						Organization org = ModelService.createModelObject(
								Organization.class, launchorg_id);
						while (!org.isFunctionDepartment()) {
							org = (Organization) org.getParentOrganization();
						}
						if (org != null) {
							org_id = org.get_id();
						}
					}
					String inputValue = (String) getInputValue("projecttemplate_id");
					if (inputValue == null) {
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目模版无法新建项目");
						return result;
					}
					ObjectId projecttemplate_id = new ObjectId(inputValue);
					Object desc = getInputValue("standloneworkdesc");
					Object description = getInputValue("standloneworkdescription");
					Object projecttype = getInputValue("projecttype");
					Object standardset = getInputValue("standardset");
					Object producttype = getInputValue("producttype");
					Object planfinish = getInputValue("standloneworkplanfinish");
					Object planstart = getInputValue("standloneworkplanstart");
					String workOrder = (String) getInputValue("prj_number");
					String prj_manager = (String) getInputValue("prj_manager");

					ProjectToolkit.doCreateNewProject(work, (String) desc,
							(String) description, launchorg_id, org_id,
							prj_manager, workOrder, projecttemplate_id,
							planfinish, planstart, projecttype, standardset,
							producttype, new BPMServiceContext(processName,
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
		return result;
	}

}
