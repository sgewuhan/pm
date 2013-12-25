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

public class ProjectReviewInputOfJSZX implements ITaskFormInputHandler {

	public ProjectReviewInputOfJSZX() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig, Task task) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			try {
				List<String> reviewer_list = (ArrayList<String>) taskForm
						.getProcessInstanceVarible("reviewer_list", //$NON-NLS-1$
								new CurrentAccountContext());
				if (reviewer_list != null && reviewer_list.size() > 0) {
					BasicDBList reviewerlist = new BasicDBList();
					reviewerlist.addAll(reviewer_list);
					taskForm.setValue("reviewer_list", reviewerlist); //$NON-NLS-1$
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return taskForm;
		}
		return null;
	}

}
