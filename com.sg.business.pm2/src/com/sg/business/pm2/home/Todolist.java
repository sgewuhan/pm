package com.sg.business.pm2.home;

import com.mobnut.db.model.DataSetFactory;
import com.sg.business.model.dataset.work.ProcessingNavigatorItemSet;
import com.sg.business.work.WorkflowSynchronizer;

public class Todolist extends AbstractListViewSideItem {
	private String userid;
	private DataSetFactory dataSet;

	public Todolist() {
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
