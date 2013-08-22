package com.sg.business.model.dataset.project;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class WorkOfProject extends MasterDetailDataSetFactory {

	public WorkOfProject() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
		setSort(new SEQSorter().getBSON());
	}

	@Override
	protected String getDetailCollectionKey() {
		return Work.F__ID;
	}


	@Override
	protected Object getMasterValue() {
		return master.getValue(Project.F_WORK_ID);
	}
	
	
}
