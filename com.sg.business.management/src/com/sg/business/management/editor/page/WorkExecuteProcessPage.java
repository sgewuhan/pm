package com.sg.business.management.editor.page;

import com.sg.business.model.WorkDefinition;
@Deprecated
public class WorkExecuteProcessPage extends AbstractWorkProcessPage {

	@Override
	protected String getWorkflowKey() {
		return WorkDefinition.F_WF_EXECUTE;
	}

	@Override
	public boolean canRefresh() {
		return false;
	}

}
