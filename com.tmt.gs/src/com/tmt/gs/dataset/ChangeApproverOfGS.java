package com.tmt.gs.dataset;

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

public class ChangeApproverOfGS extends MasterDetailDataSetFactory {

	public IContext context;
	private User user;

	public ChangeApproverOfGS() {
		super(IModelConstants.DB, IModelConstants.C_USER);
		context = new CurrentAccountContext();
		String userId = context.getAccountInfo().getUserId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	public DataSet getDataSet() {
		if (master != null) {
			if (master instanceof TaskForm) {
				List<PrimaryObject> result = new ArrayList<PrimaryObject>();
				try {
				List<PrimaryObject> roles = user
						.getRoles(IRoleConstance.ROLE_TECHNICAL_LEADER_CHECKER_ID);
				for (PrimaryObject po : roles) {
					if (po instanceof Role) {
						Role role = (Role) po;
						Organization org = role.getOrganization();
						result.add(org);
					}
				}
				return new DataSet(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.getDataSet();
	}

}
