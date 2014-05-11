package com.tmt.tb.handler;

import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.ITaskFormInputHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.TaskForm;
import com.sg.widgets.part.CurrentAccountContext;


public class ProjectApplicationAuditInputOfTB implements ITaskFormInputHandler {

	public ProjectApplicationAuditInputOfTB() {
	}

	@Override
	public PrimaryObject getTaskFormInputData(PrimaryObject taskFormData,
			TaskFormConfig taskFormConfig, Task task) {
		if(taskFormData instanceof TaskForm){
			TaskForm taskform=(TaskForm) taskFormData;
			Object prjmanager;
			Object projecttype;
			try {
				prjmanager = taskform.getProcessInstanceVarible("prj_manager", new CurrentAccountContext());
				projecttype=taskform.getProcessInstanceVarible("projecttype", new CurrentAccountContext());
				if(prjmanager instanceof String){
					taskform.setValue("prj_manager", prjmanager);
				}
				if(projecttype instanceof String){
					taskform.setValue("projecttype", projecttype);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return taskform;
		}
		return null;
	}

}
