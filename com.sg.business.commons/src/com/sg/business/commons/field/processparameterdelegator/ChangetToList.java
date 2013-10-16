package com.sg.business.commons.field.processparameterdelegator;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;

public class ChangetToList implements IProcessParameterDelegator {

	public ChangetToList() {
	}

	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		Object value = taskFormData.getValue(taskDatakey);
		System.out.println(value);
		if(value instanceof ArrayList){
			ArrayList result = new ArrayList();
			result.addAll((List<?>)value);
			return result;
		}
		return null;
	}

}
