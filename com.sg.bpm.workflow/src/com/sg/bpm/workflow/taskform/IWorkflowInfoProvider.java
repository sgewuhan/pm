package com.sg.bpm.workflow.taskform;

import com.mobnut.db.model.PrimaryObject;

public interface IWorkflowInfoProvider {
	
	Object getWorkflowInformation(PrimaryObject workData);

}
