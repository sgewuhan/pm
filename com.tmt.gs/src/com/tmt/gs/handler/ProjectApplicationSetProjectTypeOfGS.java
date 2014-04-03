package com.tmt.gs.handler;

import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.ITaskFormInputHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.TaskForm;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectApplicationSetProjectTypeOfGS implements
		ITaskFormInputHandler {

	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig, Task task) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			Object projecttype;
			Object standardset;
			Object prj_manager;
			try {
				projecttype = taskForm.getProcessInstanceVarible("projecttype", //$NON-NLS-1$
						new CurrentAccountContext());
				standardset = taskForm.getProcessInstanceVarible("standardset",
						new CurrentAccountContext());
				prj_manager = taskForm.getProcessInstanceVarible("prj_manager",
						new CurrentAccountContext());
				if (projecttype instanceof String) {
					taskForm.setValue("projecttype", projecttype);
				}
				if (prj_manager instanceof String) {
					taskForm.setValue("prj_manager", prj_manager);
				}
				if (standardset instanceof String) {
					taskForm.setValue("standardset", standardset);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return taskForm;
		}
		return null;
	}

}
