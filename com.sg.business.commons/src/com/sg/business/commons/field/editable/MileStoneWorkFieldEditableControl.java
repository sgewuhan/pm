package com.sg.business.commons.field.editable;

import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.part.editor.fields.value.IFieldEditableHandler;

public class MileStoneWorkFieldEditableControl implements IFieldEditableHandler {

	public MileStoneWorkFieldEditableControl() {
	}

	@Override
	public boolean isEditable(Object object, String key, Object value) {
		Work work = (Work) object;
		Project project = work.getProject();
		if (project != null) {
			String lc = project.getLifecycleStatus();
			if (lc.equals(ILifecycle.STATUS_WIP_VALUE )) {
				if(work.isMilestone()){
					return false;
				}
			}
		}
		
		return true;

	}

}
