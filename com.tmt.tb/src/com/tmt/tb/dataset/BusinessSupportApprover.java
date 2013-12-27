package com.tmt.tb.dataset;

import java.util.ArrayList;
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

public class BusinessSupportApprover extends MasterDetailDataSetFactory {

	private IContext context;
	private User user;
	public BusinessSupportApprover() {
		super(IModelConstants.DB, IModelConstants.C_USER);
		context = new CurrentAccountContext();
		String userId = context.getAccountInfo().getUserId();
		user = UserToolkit.getUserById(userId);
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
				try {
					Organization org = user.getOrganization();
					Role role = org.getRole(IRoleConstance.ROLE_SUPPORT_APPROVE_ID, Organization.ROLE_SEARCH_UP);
					List<PrimaryObject> assignment = role.getAssignment();
					result.addAll(assignment);
					return new DataSet(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.getDataSet();
	}
}
