package com.tmt.gs.dataset;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.taskforms.IRoleConstance;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class GSOrganizationSelectorOfTaskforms extends MasterDetailDataSetFactory {

	public GSOrganizationSelectorOfTaskforms() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	@Override
	public DataSet getDataSet() {
		if (master != null) {
			if (master instanceof TaskForm) {
				TaskForm taskForm = (TaskForm) master;
				BasicDBObject actors = (BasicDBObject) taskForm
						.getValue("wf_execute_actors"); //$NON-NLS-1$
				if (actors != null) {
					String userId = (String) actors.get("act_cheif_engineer"); //$NON-NLS-1$
					User user = UserToolkit.getUserById(userId);
					List<PrimaryObject> orgList = user
							.getRoleGrantedInAllOrganization(IRoleConstance.ROLE_PROJECR_APPROVER_ID);
					return new DataSet(orgList);
				}
			}
		}
		return super.getDataSet();
	}

}
