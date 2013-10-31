package com.tmt.tb.bpmservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.BPMServiceContext;

public class CreateProjectOfTB extends ServiceProvider {

	public CreateProjectOfTB() {
	}

	@SuppressWarnings("unchecked")
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
					Object projecttemplate_id = null;
					Object desc = null;
					Object projecttype = null;
					Object standardset = null;
					Object producttype = null;
					Object planfinish = null;
					Object planstart = null;
					List<Map<String, Object>> historys = (List<Map<String, Object>>) work
							.getValue(IWorkCloneFields.F_WF_EXECUTE
									+ IProcessControl.POSTFIX_HISTORY);
					for (Map<String, Object> history : historys) {
						String taskname = (String) history
								.get(IProcessControl.F_WF_TASK_NAME);
						if ("批准".equals(taskname)) {
							ObjectId form_dept = (ObjectId) history
									.get("form_dept");
							if (form_dept != null) {
								Organization org = ModelService
										.createModelObject(Organization.class,
												form_dept);
								launchorg_id = org.get_id();
								while (!org.isFunctionDepartment()) {
									org = (Organization) org
											.getParentOrganization();
								}
								if (org != null) {
									org_id = org.get_id();
								}
							}
						} else if ("创建项目".equals(taskname)) {
							Object form_projecttemplate_id = history
									.get("form_projecttemplate_id");
							if (form_projecttemplate_id != null) {
								projecttemplate_id = form_projecttemplate_id;
							}
							Object form_standloneworkdesc = history
									.get("form_standloneworkdesc");
							if (form_standloneworkdesc != null) {
								desc = form_standloneworkdesc;
							}
							Object form_projecttype = history
									.get("form_projecttype");
							if (form_projecttype != null) {
								projecttype = form_projecttype;
							}
							Object form_standardset = history
									.get("form_standardset");
							if (form_standardset != null) {
								standardset = form_standardset;
							}
							Object form_producttype = history
									.get("form_standardset");
							if (form_producttype != null) {
								producttype = form_producttype;
							}
							Object form_standloneworkplanfinish = history
									.get("form_standloneworkplanfinish");
							if(form_standloneworkplanfinish != null){
								planfinish = form_standloneworkplanfinish;
							}
							Object form_standloneworkplanstart = history
									.get("form_standloneworkplanstart");
							if(form_standloneworkplanstart != null){
								planstart = form_standloneworkplanstart;
							}
						}
					}
					String workOrder = (String) work.getValue("prj_number");
					String prj_manager = (String) work.getValue("prj_manager");
					
					if(workOrder == null ){
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入工作令号无法新建项目");
						return result;
					}
					if(prj_manager == null ){
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目经理无法新建项目");
						return result;
					}
					if(desc == null ){
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目名称无法新建项目");
						return result;
					}
					if(launchorg_id == null ){
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目发起部门无法新建项目");
						return result;
					}
					if(org_id == null ){
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目管理部门无法新建项目");
						return result;
					}
					if(projecttemplate_id == null ){
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目模版无法新建项目");
						return result;
					}
					if(planfinish == null ){
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目计划完成时间无法新建项目");
						return result;
					}
					if(planstart == null ){
						result.put("returnCode", "ERROR");
						result.put("returnMessage", "未录入项目计划开始时间无法新建项目");
						return result;
					}
					
					Project project = ModelService
							.createModelObject(Project.class);
					project.setValue(Project.F_DESC, desc);
					project.setValue(Project.F_LAUNCH_ORGANIZATION,
							launchorg_id);
					project.setValue(Project.F_FUNCTION_ORGANIZATION, org_id);
					project.setValue(Project.F_CHARGER, prj_manager);
					project.setValue(Project.F_WORK_ORDER, workOrder);
					project.setValue(Project.F_PROJECT_TEMPLATE_ID,
							projecttemplate_id);
					project.setValue(Project.F_PLAN_FINISH,
							planfinish);
					project.setValue(Project.F_PLAN_START,
							planstart);

					if (projecttype != null) {
						project.setValue(Project.F_PROJECT_TYPE_OPTION,
								projecttype);
					}
					if (standardset != null) {
						project.setValue(Project.F_STANDARD_SET_OPTION,
								standardset);
					}
					if (producttype != null) {
						project.setValue(Project.F_PRODUCT_TYPE_OPTION,
								producttype);
					}

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
