package com.tmt.tb.dataset;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.TaskForm;
import com.sg.business.taskforms.IRoleConstance;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class BusinessSupportApprover extends MasterDetailDataSetFactory {

	public BusinessSupportApprover() {
		super(IModelConstants.DB, IModelConstants.C_USER);
	}

	@Override
	protected String getDetailCollectionKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataSet getDataSet() {
		if (master != null) {
			if (master instanceof TaskForm) {
				List<PrimaryObject> result = new ArrayList<PrimaryObject>();
				TaskForm taskForm = (TaskForm) master;
				ObjectId org_id = (ObjectId) taskForm.getValue("dept");
				if (org_id != null) {
					try {
						Organization org = ModelService.createModelObject(
								Organization.class, org_id);
						Role role = org.getRole(
								IRoleConstance.ROLE_SUPPORT_APPROVE_ID,
								Organization.ROLE_SEARCH_UP);
						List<PrimaryObject> assignment = role.getAssignment();
						result.addAll(assignment);
						return new DataSet(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return super.getDataSet();
	}
}
