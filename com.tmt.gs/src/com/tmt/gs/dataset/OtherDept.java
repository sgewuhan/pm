package com.tmt.gs.dataset;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class OtherDept extends MasterDetailDataSetFactory {

	public IContext context;
	private User user;

	public OtherDept() {
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
				List<PrimaryObject> result=new ArrayList<PrimaryObject>();
				List<PrimaryObject> prjlist = user.getChargeProject(ILifecycle.STATUS_NONE_VALUE);
				for (PrimaryObject po : prjlist) {
					if(po instanceof Project){
						Project project=(Project) po;
						Organization org=project.getFunctionOrganization();
						result.add(org);
					}
					
				}
				return new DataSet(result);
			}
		}
		return super.getDataSet();

	}
}
