package com.tmt.tb.dataset;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.TaskForm;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class StandloneWorkOfTB extends OptionDataSetFactory {

	private IContext context;

	public StandloneWorkOfTB() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
		context = new CurrentAccountContext();
	}

	@Override
	public void setEditorData(PrimaryObject data) {
		// 获取当前项目所属的项目职能组织
		TaskForm taskForm = (TaskForm) data;
		ObjectId org_id = null;
		try {
			String obj = (String) taskForm.getProcessInstanceVarible("dept",
					context);
			if (obj != null) {
				ObjectId deptid = new ObjectId(obj);
				Organization org = ModelService.createModelObject(
						Organization.class, deptid);
				while (!org.isFunctionDepartment()) {
					org = (Organization) org.getParentOrganization();
				}
				org_id = org.get_id();
				// 设置查询条件为所属组织或当前组织
				if (org_id != null) {
					setQueryCondition(new BasicDBObject().append(
							ProjectTemplate.F_ORGANIZATION_ID, org_id).append(
							ProjectTemplate.F_ACTIVATED, Boolean.TRUE));
				} else {
					setQueryCondition(new BasicDBObject().append(
							ProjectTemplate.F__ID, null));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.setEditorData(data);
	}
}
