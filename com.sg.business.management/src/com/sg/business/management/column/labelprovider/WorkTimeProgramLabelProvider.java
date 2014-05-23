package com.sg.business.management.column.labelprovider;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.WorkTimeProgram;
import com.sg.widgets.commons.labelprovider.ObjectIdLabelProvider;

public class WorkTimeProgramLabelProvider extends ObjectIdLabelProvider {

	public WorkTimeProgramLabelProvider() {
	}

	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return WorkTimeProgram.class;
	}

}
