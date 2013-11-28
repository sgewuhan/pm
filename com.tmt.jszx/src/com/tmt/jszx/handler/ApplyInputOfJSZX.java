package com.tmt.jszx.handler;

import org.jbpm.task.Task;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.ITaskFormInputHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.Organization;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class ApplyInputOfJSZX implements ITaskFormInputHandler {

	private static IContext context;

	public ApplyInputOfJSZX() {
		context = new CurrentAccountContext();
	}

	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig, Task task) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			try {
				if (context == null) {
					context = new CurrentAccountContext();
				}
				String consignerId = context.getAccountInfo().getConsignerId();
				User user = UserToolkit.getUserById(consignerId);
				Organization org = user.getOrganization();
				taskForm.setValue("dept", org.get_id());

			} catch (Exception e) {
				e.printStackTrace();
			}
			return taskForm;
		}
		return null;
	}
}
