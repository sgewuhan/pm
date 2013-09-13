package com.sg.bpm.workflow.taskform;

import java.util.Map;

import com.mobnut.db.model.PrimaryObject;

public interface IWorkflowInfoProvider {
	
	Map<String,Object> getWorkflowInformation(PrimaryObject taskform);

}
