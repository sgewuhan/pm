package com.sg.bpm.workflow.taskform;

import com.mobnut.db.model.PrimaryObject;


public interface IProcessParameterDelegator {

	Object getValue(String processParameter, String taskDatakey, PrimaryObject taskFormData);

}
