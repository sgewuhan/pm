package com.tmt.xt.dataset;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.taskforms.IRoleConstance;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class UserOfSupportApprove extends MasterDetailDataSetFactory {

	private IContext context;

	public UserOfSupportApprove() {
		super(IModelConstants.DB, IModelConstants.C_ROLE_ASSIGNMENT);
		context = new CurrentAccountContext();
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	@Override
	public DataSet getDataSet() {
		if (master != null) {
			if (master instanceof TaskForm) {
				try {
					List<PrimaryObject> result;
					String userId = context.getAccountInfo().getUserId();
					User user = UserToolkit.getUserById(userId);
					Organization org = user.getOrganization();
					Role role = org.getRole(IRoleConstance.ROLE_SUPPORT_APPROVE_ID, Organization.ROLE_SEARCH_UP);
					result = role.getAssignment();
					if (result != null && result.size() > 0) {
						return new DataSet(result);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.getDataSet();
	}

}
