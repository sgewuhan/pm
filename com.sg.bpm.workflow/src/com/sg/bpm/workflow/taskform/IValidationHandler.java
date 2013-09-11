package com.sg.bpm.workflow.taskform;

import com.mobnut.db.model.PrimaryObject;

public interface IValidationHandler {

	boolean validate(PrimaryObject work);

	String getMessage();
}
