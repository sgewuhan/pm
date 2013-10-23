package com.sg.business.taskforms.handler;

import java.util.List;
import java.util.Map;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.ITaskFormInputHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;

public class ProjectReviewConvenerOfJSZX implements ITaskFormInputHandler {

	public ProjectReviewConvenerOfJSZX() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			Work work = taskForm.getWork();
			List<Map<String, Object>> historys = (List<Map<String, Object>>) work
					.getValue(IWorkCloneFields.F_WF_EXECUTE
							+ IProcessControl.POSTFIX_HISTORY);
			for (Map<String, Object> history : historys) {
				String taskname = (String) history
						.get(IProcessControl.F_WF_TASK_NAME);
				if ("�ύ����".equals(taskname)) {
					List<?> form_reviewer_list = (List<?>) history
							.get("form_reviewer_list");
					if (form_reviewer_list != null) {
						taskForm.setValue("reviewer_list",
								(Object) form_reviewer_list);
					}
				} else if ("��׼����".equals(taskname)) {
					String form_review_convener = (String) history
							.get("form_review_convener");
					if (form_review_convener != null) {
						taskForm.setValue("review_convener",
								(Object) form_review_convener);
					}
				}
			}
			return taskForm;
		}
		return null;
	}

}
