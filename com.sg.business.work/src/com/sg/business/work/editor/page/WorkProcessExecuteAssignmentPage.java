package com.sg.business.work.editor.page;

import com.sg.business.model.WorkDefinition;

public class WorkProcessExecuteAssignmentPage extends AbstractWorkProcessExecutPage {

	@Override
	protected String getWorkflowKey() {
		return WorkDefinition.F_WF_EXECUTE;
	}
}
