package com.sg.business.management.editor.page;

import com.sg.business.model.WorkDefinition;

public class WorkExecuteProcessPage extends AbstractWorkProcessPage {

	@Override
	protected String getWorkflowKey() {
		return WorkDefinition.F_WF_EXECUTE;
	}

}
