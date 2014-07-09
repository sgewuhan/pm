package com.sg.business.model.dataset;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkTimeProgram;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class WorkTimeProgramOfProject extends MasterDetailDataSetFactory {

	public WorkTimeProgramOfProject() {
		super(IModelConstants.DB, IModelConstants.C_WORKTIMEPROGRAM);
	}

	@Override
	protected String getDetailCollectionKey() {
		return WorkTimeProgram.F__ID;
	}

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		result.add(master);
		return result;
	}



}
