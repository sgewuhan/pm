package com.sg.business.management.editor.page;

import com.sg.business.model.WorkDefinition;
@Deprecated
public class WorkChangeProcessPage extends AbstractWorkProcessPage {

	@Override
	protected String getWorkflowKey() {
		return WorkDefinition.F_WF_CHANGE;
	}

	@Override
	public boolean canRefresh() {
		return false;
	}


}
