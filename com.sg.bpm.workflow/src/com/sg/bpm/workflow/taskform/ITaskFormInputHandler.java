package com.sg.bpm.workflow.taskform;

import com.mobnut.db.model.PrimaryObject;


public interface ITaskFormInputHandler {

	PrimaryObject getTaskFormInputData(PrimaryObject taskFormData, TaskFormConfig taskFormConfig);

}
