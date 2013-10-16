package com.sg.business.commons.field.processparameterdelegator;

import java.util.ArrayList;

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
			return (ArrayList<?>)value;
		}
		return null;
	}

}
