package com.tmt.gs.handler;

import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.ITaskFormInputHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.TaskForm;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectChangeOfGSInputHandler implements ITaskFormInputHandler {

	public ProjectChangeOfGSInputHandler() {
	}

	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig, Task task) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			try {
				String project_manager = (String) taskForm
						.getProcessInstanceVarible("project_manager", //$NON-NLS-1$
								new CurrentAccountContext());
				if (project_manager != null ) {
					taskForm.setValue("project_manager", project_manager); //$NON-NLS-1$
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return taskForm;
		}
		return null;
	}

}
