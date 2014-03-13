package com.sg.business.commons.column.editingsupport;

import com.sg.business.model.Work;

public class PlanStartEditing extends SchedualEditing {

	@Override
	protected String getFieldName() {
		return Work.F_PLAN_START;
	}

}
