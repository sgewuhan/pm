package com.tmt.tb.dataset;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class OrganizationSelectorOfTaskforms extends MasterDetailDataSetFactory {

	public OrganizationSelectorOfTaskforms() {
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
				String userId = new CurrentAccountContext().getAccountInfo()
						.getConsignerId();;
				User user = UserToolkit.getUserById(userId );
				List<PrimaryObject> orgList = user.getRoleGrantedInAllOrganization(Role.ROLE_PROJECR_APPROVER_ID);
				return new DataSet(orgList);
			}
		}
		return super.getDataSet();
	}

}
