package com.sg.bpm.workflow.taskform;

import com.mobnut.db.model.PrimaryObject;

public interface IValidationHandler {

	boolean validateBeforeOpen(PrimaryObject work);

	String getMessage();
}
