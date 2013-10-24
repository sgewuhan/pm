package com.sg.business.taskforms.handler;

import java.util.Collection;

import org.drools.runtime.process.NodeInstance;
import org.drools.runtime.process.WorkflowProcessInstance;
import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.bpm.workflow.taskform.ITaskFormInputHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;


public class ProjectReviewReviewOfJSZX implements ITaskFormInputHandler {

	public ProjectReviewReviewOfJSZX() {
	}

	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig, Task task) {
		long processInstanceId = task.getTaskData().getProcessInstanceId();
		String processId = task.getTaskData().getProcessId();
		try {
			WorkflowProcessInstance process = WorkflowService.getDefault().getProcessInstance(processId, processInstanceId);
			Collection<NodeInstance> nodeInstances = process.getNodeInstances();
			
			Object reviewer = process.getVariable("reviewer");
			System.out.println(reviewer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskFormData;
	}

	
}
