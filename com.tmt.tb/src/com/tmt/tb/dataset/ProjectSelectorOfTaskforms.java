package com.tmt.tb.dataset;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectSelectorOfTaskforms extends MasterDetailDataSetFactory {

	public ProjectSelectorOfTaskforms() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
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
				try {
					String obj = (String) taskForm.getProcessInstanceVarible(
							"prj_manager", new CurrentAccountContext());
					if (obj != null) {
						User user = UserToolkit.getUserById(obj);
						List<PrimaryObject> projectList = user
								.getChargeProject(ILifecycle.STATUS_NONE_TEXT);
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
