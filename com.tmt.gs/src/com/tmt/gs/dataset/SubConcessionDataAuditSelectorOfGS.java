package com.tmt.gs.dataset;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class SubConcessionDataAuditSelectorOfGS extends
		MasterDetailDataSetFactory {

	public IContext context;
	private User user;

	public SubConcessionDataAuditSelectorOfGS() {
		super(IModelConstants.DB, IModelConstants.C_USER);
		context = new CurrentAccountContext();
		String userId = context.getAccountInfo().getUserId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	@Override
	public DataSet getDataSet() {
		if (master != null) {
			if (master instanceof TaskForm) {
				List<PrimaryObject> result = new ArrayList<PrimaryObject>();
				Organization organization = user.getOrganization();
				Role role = organization.getRole(Role.ROLE_PROJECT_ADMIN_ID,
						Organization.ROLE_SEARCH_UP);
				List<PrimaryObject> assignment = role.getAssignment();
				for (int i = 0; i < assignment.size(); i++) {
					RoleAssignment assign = (RoleAssignment) assignment.get(i);
					String userid = assign.getUserid();
					User pm = UserToolkit.getUserById(userid);
					result.add(pm);
				}
				/*for (PrimaryObject primaryObject : assignment) {
					if(primaryObject instanceof User){
						User user=(User) primaryObject;
					}
					
				}*/
				
				return new DataSet(result);
			}
		}
		return super.getDataSet();
	}
}
