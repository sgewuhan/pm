package com.sg.business.model.roleparameter;

import java.util.HashMap;
import java.util.Map;

import com.sg.business.model.IRoleParameter;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.Work;

public class TaskFormRoleParameter implements IRoleParameter {
	private TaskForm taskForm;

	public TaskFormRoleParameter(TaskForm taskForm) {
		this.taskForm = taskForm;
	}

	@Override
	public Map<String, Object> getParameters() {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		Work work = taskForm.getWork();
		parameters.put(IRoleParameter.TYPE, IRoleParameter.TYPE_WORK_PROCESS);
		parameters.put(IRoleParameter.WORK_ID, work.get_id());
		parameters.put(IRoleParameter.WORK, work);
		User charger = work.getCharger();
		if (charger != null) {
			parameters.put(IRoleParameter.WORK_CHARGER, work.getCharger()
					.getUserid());
		} else {
			parameters.put(IRoleParameter.WORK_CHARGER, "");
		}
		parameters.put(IRoleParameter.WORK_MILESTONE, work.isMilestone());
		parameters.put(IRoleParameter.WORK_TYPE, work.getWorkType());
		return parameters;
	}

}
