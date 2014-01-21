package com.sg.business.model.dataset.projecttemplate;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IDataSetFactory;
import com.mobnut.db.model.PrimaryObject;

public class LaunchableWorkSet implements IDataSetFactory{

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		return null;
	}

	@Override
	public long getTotalCount() {
		return 0;
	}

	@Override
	public DataSet getDataSet() {
		return null;
	}

}
