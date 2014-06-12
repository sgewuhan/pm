package com.sg.business.management.editor;

import com.sg.business.model.WorkTimeProgram;

public class ParaXPageDelegator extends AbstractTypePageDelegator {

	public ParaXPageDelegator() {
	}

	@Override
	protected String getFieldName() {
		return WorkTimeProgram.F_WORKTIME_PARA_X;
	}

}
