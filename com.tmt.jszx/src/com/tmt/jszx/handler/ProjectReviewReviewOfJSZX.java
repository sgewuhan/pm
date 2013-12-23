package com.tmt.jszx.handler;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
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
				List<String> reviewer_List = (ArrayList<String>) taskForm
						.getProcessInstanceVarible("reviewer_list", //$NON-NLS-1$
								new CurrentAccountContext());
				if (reviewer_List != null && reviewer_List.size() > 0) {
					BasicDBList reviewerList = new BasicDBList();
					reviewerList.addAll(reviewer_List);
					taskForm.setValue("reviewer_list", reviewerList); //$NON-NLS-1$
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return taskForm;
		}
		return null;
	}

}
