package com.tmt.xt.dataset;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectSelectorOfXT extends MasterDetailDataSetFactory {

	private IContext context;

	public ProjectSelectorOfXT() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
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
					String userid = context.getAccountInfo().getConsignerId();
					if (userid != null) {
						User user = UserToolkit.getUserById(userid);
						List<PrimaryObject> projectList = user
								.getChargeProject(null);
						if (projectList != null && projectList.size() > 0) {
							return new DataSet(projectList);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.getDataSet();
	}

}
