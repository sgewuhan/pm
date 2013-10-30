package com.tmt.tb.dataset;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

public class StandloneWorkOfTB extends OptionDataSetFactory {

	public StandloneWorkOfTB() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
	}

	@Override
	public void setEditorData(PrimaryObject data) {
		// 获取当前项目所属的项目职能组织
		TaskForm taskForm = (TaskForm) data;
		Work work = taskForm.getWork();
		IProcessControl ip = work.getAdapter(IProcessControl.class);
		BasicBSONList historys = ip.getWorkflowHistroyData(
				IWorkCloneFields.F_WF_EXECUTE, true);
		ObjectId org_id = null;
		for (int i = 0; i < historys.size(); i++) {
			DBObject history = (DBObject) historys.get(i);
			String taskname = (String) history
					.get(IProcessControl.F_WF_TASK_NAME);
			if ("批准".equals(taskname)) {
				ObjectId deptid = (ObjectId) history
						.get("form_dept");
				if (deptid != null) {
					Organization org = ModelService.createModelObject(Organization.class, deptid);
					while(!org.isFunctionDepartment()){
						org = (Organization) org.getParentOrganization();
					}
					org_id = org.get_id();
					continue;
				}
			}
		}
		
		// 设置查询条件为所属组织或当前组织
		if (org_id != null) {
			setQueryCondition(new BasicDBObject().append(
					ProjectTemplate.F_ORGANIZATION_ID, org_id).append(
					ProjectTemplate.F_ACTIVATED, Boolean.TRUE));
		} else {
			setQueryCondition(new BasicDBObject().append(ProjectTemplate.F__ID,
					null));
		}
		super.setEditorData(data);
	}
}
