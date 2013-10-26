package com.tmt.jszx.dataset;

import com.mobnut.db.model.DataSet;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.TaskForm;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class TaskFormChiefMaster extends MasterDetailDataSetFactory {

	public TaskFormChiefMaster() {
		super(IModelConstants.DB, IModelConstants.C_USER);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	@Override
	public DataSet getDataSet() {
		if (master != null) {
			if (master instanceof TaskForm) {
				@SuppressWarnings("unused")
				TaskForm taskForm = (TaskForm) master;
			}
		}
		return super.getDataSet();
	}
}