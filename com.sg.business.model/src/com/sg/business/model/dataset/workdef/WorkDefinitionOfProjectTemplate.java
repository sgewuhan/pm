package com.sg.business.model.dataset.workdef;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class WorkDefinitionOfProjectTemplate extends MasterDetailDataSetFactory {

	public WorkDefinitionOfProjectTemplate() {
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
		setSort(new SEQSorter().getBSON());
	}

	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinition.F__ID;
	}


	@Override
	protected Object getMasterValue() {
		return master.getValue(ProjectTemplate.F_WORK_DEFINITON_ID);
	}
	
	
}
