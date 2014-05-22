package com.sg.business.model.dataset;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkTimeProgram;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class SelectWorkTimeProgramDataSetFactory extends
		MasterDetailDataSetFactory {

	public SelectWorkTimeProgramDataSetFactory() {
		super(IModelConstants.DB, IModelConstants.C_WORKTIMEPROGRAM);
	}

	@Override
	protected String getDetailCollectionKey() {
		return WorkTimeProgram.F_ORGANIZATION_ID;
	}

}
