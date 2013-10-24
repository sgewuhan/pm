package com.tmt.jszx.handler;

import java.util.List;

import org.bson.types.BasicBSONList;
import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.taskform.ITaskFormInputHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;

public class ProjectReviewConvenerOfJSZX implements ITaskFormInputHandler {

	public ProjectReviewConvenerOfJSZX() {
	}

	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig, Task task) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			Work work = taskForm.getWork();
			IProcessControl ip = work.getAdapter(IProcessControl.class);
			BasicBSONList historys = ip.getWorkflowHistroyData(
					IWorkCloneFields.F_WF_EXECUTE, true);
			// List<Map<String, Object>> historys = (List<Map<String, Object>>)
			// work
			// .getValue(IWorkCloneFields.F_WF_EXECUTE
			// + IProcessControl.POSTFIX_HISTORY);
			long wk = task.getTaskData().getWorkItemId();
			for (int i = 0; i < historys.size(); i++) {
				DBObject history = (DBObject) historys.get(i);
				String taskname = (String) history
						.get(IProcessControl.F_WF_TASK_NAME);
				if ("提交评审".equals(taskname)) {
					List<?> form_reviewer_list = (List<?>) history
							.get("form_reviewer_list");
					if (form_reviewer_list != null) {
						taskForm.setValue("reviewer_list",
								(Object) form_reviewer_list);
					}
				} else if ("批准评审".equals(taskname)) {
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
