package com.sg.business.model.dataset.work;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorksPerformence;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class WorksPerformenceOfWorkDataSet extends MasterDetailDataSetFactory {

	public WorksPerformenceOfWorkDataSet() {
		super(IModelConstants.DB,IModelConstants.C_WORKS_PERFORMENCE);
	}

	@Override
	protected String getDetailCollectionKey() {
		return WorksPerformence.F_WORKID;
	}

}
