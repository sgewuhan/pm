package com.sg.business.commons.ui.sidebar;

import com.mobnut.db.model.DataSetFactory;
import com.sg.business.model.WorkflowSynchronizer;
import com.sg.business.model.dataset.work.ProcessingNavigatorItemSet;

public class PerformenceMenu extends AbstractListViewSideItem {
	private String userid;
	private DataSetFactory dataSet;

	public PerformenceMenu() {
		super();
		userid = context.getAccountInfo().getConsignerId();
		dataSet = new ProcessingNavigatorItemSet();
	}

	@Override
	protected DataSetFactory getDataSetFactory() {

		WorkflowSynchronizer synchronizer = new WorkflowSynchronizer();
		synchronizer.synchronizeUserTask(userid);

		return dataSet;
	}

}