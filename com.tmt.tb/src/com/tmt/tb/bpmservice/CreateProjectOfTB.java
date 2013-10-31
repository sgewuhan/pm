package com.tmt.tb.bpmservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.BPMServiceContext;

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

					if (workOrder == null) {
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入工作令号无法新建项目");
						return result;
					}
					if (prj_manager == null) {
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目经理无法新建项目");
						return result;
					}
					if (desc == null) {
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目名称无法新建项目");
						return result;
					}
					if (launchorg_id == null) {
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目发起部门无法新建项目");
						return result;
					}
					if (org_id == null) {
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目管理部门无法新建项目");
						return result;
					}
					if (inputValue == null) {
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目模版无法新建项目");
						return result;
					}
					if (planfinish == null) {
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目计划完成时间无法新建项目");
						return result;
					}
					if (planstart == null) {
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目计划开始时间无法新建项目");
						return result;
					}

					BasicDBObject projectObject = new BasicDBObject();
					projectObject.put(Project.F_DESC, desc);
					projectObject.put(Project.F_DESCRIPTION, description);
					projectObject.put(Project.F_LAUNCH_ORGANIZATION,
							launchorg_id);
					projectObject.put(Project.F_FUNCTION_ORGANIZATION, org_id);
					projectObject.put(Project.F_CHARGER, prj_manager);
					projectObject.put(Project.F_WORK_ORDER, workOrder);
					projectObject.put(Project.F_PROJECT_TEMPLATE_ID,
							projecttemplate_id);
					projectObject.put(Project.F_PLAN_FINISH, planfinish);
					projectObject.put(Project.F_PLAN_START, planstart);

					if (projecttype != null) {
						projectObject.put(Project.F_PROJECT_TYPE_OPTION,
								projecttype);
					}
					if (standardset != null) {
						projectObject.put(Project.F_STANDARD_SET_OPTION,
								standardset);
					}
					if (producttype != null) {
						projectObject.put(Project.F_PRODUCT_TYPE_OPTION,
								producttype);
					}
					List<PrimaryObject> projectList = work
							.getRelationByCondition(Project.class,
									projectObject);
					if (projectList != null && projectList.size() > 0) {
						return result;
					}

					Project project = ModelService.createModelObject(
							projectObject, Project.class);
					project.doSave(new BPMServiceContext(processName, processId));
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
