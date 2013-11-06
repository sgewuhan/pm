package com.tmt.jszx.handler;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.ITaskFormInputHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.TaskForm;
import com.sg.widgets.part.CurrentAccountContext;


public class ProjectReviewReviewOfJSZX implements ITaskFormInputHandler {

	public ProjectReviewReviewOfJSZX() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig, Task task) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			try {
				List<String> reviewerList = (ArrayList<String>) taskForm
						.getProcessInstanceVarible("reviewer_list",
								new CurrentAccountContext());
				if (reviewerList != null && reviewerList.size() > 0) {
					taskForm.setValue("reviewer_list", (Object) reviewerList);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return taskForm;
		}
		return null;
	}

	
}
