package com.sg.business.commons.field.processparameter;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;

public class ChangetToList implements IProcessParameterDelegator {

	@SuppressWarnings("unchecked")
	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		Object value = taskFormData.getValue(taskDatakey);
		if (value instanceof ArrayList) {
			List<String> result = new ArrayList<String>();
			result.addAll((ArrayList<String>) value);
			return result;
		}
		return "";
	}

}
