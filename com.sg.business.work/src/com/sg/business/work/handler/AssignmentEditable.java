package com.sg.business.work.handler;

import com.sg.business.model.ILifecycle;
import com.sg.business.model.Work;
import com.sg.widgets.part.editor.fields.value.IFieldEditableHandler;

public class AssignmentEditable implements IFieldEditableHandler {


	@Override
	public boolean isEditable(Object data, String key, Object value) {
		if(data instanceof Work){
			Work work = (Work) data;
			String status = work.getLifecycleStatus();
			if(ILifecycle.STATUS_NONE_VALUE.equals(status)||ILifecycle.STATUS_ONREADY_VALUE.equals(status)){
				return true;
			}
			
		}
		
		return false;
	}

}
