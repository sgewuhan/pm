package com.tmt.xt.handler;

import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.ITaskFormInputHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.TaskForm;
import com.sg.widgets.part.CurrentAccountContext;


public class ProjectApplicationSetProjectTypeOfXT implements
		ITaskFormInputHandler {


	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig, Task task) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			Object projecttype;
			try {
				projecttype = taskForm.getProcessInstanceVarible("projecttype", //$NON-NLS-1$
						new CurrentAccountContext());
				if (projecttype instanceof String) {
					taskForm.setValue("projecttype", projecttype); 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return taskForm;
		}
		return null;
	}

}
