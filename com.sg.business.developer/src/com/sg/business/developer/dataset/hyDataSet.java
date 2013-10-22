package com.sg.business.developer.dataset;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IDataSetFactory;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.developer.model.hy1;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class hyDataSet extends MasterDetailDataSetFactory  {

	public hyDataSet() {
		super(IModelConstants.DB, "hy1");
	}

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTotalCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DataSet getDataSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailCollectionKey() {
		// TODO Auto-generated method stub
		return  hy1.F_DEMO1_ID;
	}

}
