package com.sg.business.work.editor.page;

import com.sg.business.model.WorkDefinition;

public class WorkProcessChangePage extends AbstractWorkProcessExecutPage {

	@Override
	protected String getWorkflowKey() {
		return WorkDefinition.F_WF_CHANGE;
	}
}
