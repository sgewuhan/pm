package com.sg.business.management.editor;

import com.sg.business.model.WorkTimeProgram;

public class ColumnTypePageDelegator extends AbstractTypePageDelegator {

	public ColumnTypePageDelegator() {
	}

	@Override
	protected String getFieldName() {
		return WorkTimeProgram.F_COLUMNTYPES;
	}

}
