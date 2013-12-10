package com.sg.business.commons.field.editable;

import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
import com.sg.widgets.part.editor.fields.value.IFieldEditableHandler;

public class ProjectFieldEditableControl implements IFieldEditableHandler {

	public ProjectFieldEditableControl() {
	}

	@Override
	public boolean isEditable(Object object, String key, Object value) {
		Project project = (Project) object;
		if (project != null) {
			String lc = project.getLifecycleStatus();
			if (lc.equals(ILifecycle.STATUS_WIP_VALUE)) {
				return false;
			}
		}

		return true;
	}

}
