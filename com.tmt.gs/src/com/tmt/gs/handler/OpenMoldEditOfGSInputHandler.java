package com.tmt.gs.handler;

import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.ITaskFormInputHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.TaskForm;
import com.sg.widgets.part.CurrentAccountContext;

public class OpenMoldEditOfGSInputHandler implements ITaskFormInputHandler {

	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig, Task task) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskform = (TaskForm) taskFormData;
			Object moldamount;
			Object moldask;
			try {
				moldamount = taskform.getProcessInstanceVarible("mold_amount",
						new CurrentAccountContext());
				moldask = taskform.getProcessInstanceVarible("mold_ask",
						new CurrentAccountContext());
				if (moldamount instanceof Double) {
					taskform.setValue("mold_amount", moldamount);
				}
				if (moldask instanceof String) {
					taskform.setValue("mold_ask", moldask);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return taskform;
		}
		return null;
	}

}
