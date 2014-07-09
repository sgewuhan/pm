package com.sg.business.project.field.editable;

import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.part.editor.fields.value.IFieldEditableHandler;

public class MeasureFieldEditableControl implements IFieldEditableHandler {

	public MeasureFieldEditableControl() {
	}

	@Override
	public boolean isEditable(Object object, String key, Object value) {
		Work work = (Work) object;
		Project project = work.getProject();
		if (project != null) {
			String lc = project.getLifecycleStatus();
			if (!lc.equals(ILifecycle.STATUS_NONE_VALUE)) {
				return false;
			}
		}

		return true;
	}
}
