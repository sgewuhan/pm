package com.sg.business.management.editor;

import com.sg.business.model.WorkTimeProgram;

public class WorkTimeTypePageDelegator extends AbstractTypePageDelegator {

	public WorkTimeTypePageDelegator() {
	}

	@Override
	protected String getFieldName() {
		return WorkTimeProgram.F_WORKTIMETYPES;
	}

}
