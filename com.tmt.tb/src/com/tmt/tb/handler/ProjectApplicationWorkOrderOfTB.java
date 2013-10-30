package com.tmt.tb.handler;

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

public class ProjectApplicationWorkOrderOfTB implements ITaskFormInputHandler {

	public ProjectApplicationWorkOrderOfTB() {
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
			for (int i = 0; i < historys.size(); i++) {
				DBObject history = (DBObject) historys.get(i);
				String taskname = (String) history
						.get(IProcessControl.F_WF_TASK_NAME);
				if ("Á¢ÏîÉêÇë".equals(taskname)) {
					String workOrder = (String) history
							.get("form_prj_number");
					if (workOrder != null) {
						taskForm.setValue("prj_number",
								 workOrder);
					}
				}
			}
			return taskForm;
		}
		return null;
	}

}
