package com.tmt.gs.handler;

import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.ITaskFormInputHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.TaskForm;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectApplicationWorkOrderOfGS implements ITaskFormInputHandler {

	public ProjectApplicationWorkOrderOfGS() {
	}

	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig, Task task) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			Object workOrder;
			try {
				workOrder = taskForm.getProcessInstanceVarible("prj_number", //$NON-NLS-1$
						new CurrentAccountContext());
				if (workOrder instanceof String) {
					taskForm.setValue("prj_number", workOrder); 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return taskForm;
		}
		return null;
	}

}
