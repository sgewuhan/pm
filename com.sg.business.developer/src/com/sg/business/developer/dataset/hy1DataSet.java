package com.sg.business.developer.dataset;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IDataSetFactory;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.developer.model.hy;
import com.sg.business.model.IModelConstants;

public class hy1DataSet extends SingleDBCollectionDataSetFactory {

	public hy1DataSet() {
		// TODO Auto-generated constructor stub
		super(IModelConstants.DB, "hy");
		setQueryCondition(new BasicDBObject().append(hy.F_PARENT_ID, null));
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

}
