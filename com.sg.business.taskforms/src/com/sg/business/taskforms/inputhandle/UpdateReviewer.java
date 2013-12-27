package com.sg.business.taskforms.inputhandle;

import java.util.List;

import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.ITaskFormInputHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.TaskForm;
import com.sg.widgets.part.CurrentAccountContext;

public class UpdateReviewer implements ITaskFormInputHandler {

	public UpdateReviewer() {
	}

	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig, Task task) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			Object reviewer_list;
			Object review_convener;
			try {
				reviewer_list = taskForm.getProcessInstanceVarible("reviewer_list", //$NON-NLS-1$
						new CurrentAccountContext());
				review_convener = taskForm.getProcessInstanceVarible("review_convener", //$NON-NLS-1$
						new CurrentAccountContext());
				if (reviewer_list instanceof List) {
					taskForm.setValue("reviewer_list", reviewer_list); //$NON-NLS-1$
				}
				if (review_convener instanceof String) {
					taskForm.setValue("review_convener", review_convener); //$NON-NLS-1$
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return taskForm;
		}
		return null;
	}
}