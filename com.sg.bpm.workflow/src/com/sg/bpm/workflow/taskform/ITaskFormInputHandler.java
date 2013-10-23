package com.sg.bpm.workflow.taskform;

import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;


public interface ITaskFormInputHandler {

	PrimaryObject getTaskFormInputData(PrimaryObject taskFormData, TaskFormConfig taskFormConfig);

	void setTast(Task task);

}
