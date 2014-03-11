package com.sg.sales.field.editable;

import com.sg.sales.model.TeamControl;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.fields.value.IFieldEditableHandler;

public class TeamControledEditable implements IFieldEditableHandler {

	public TeamControledEditable() {
	}

	@Override
	public boolean isEditable(Object data, String key, Object value) {
		String userId = new CurrentAccountContext().getConsignerId();
		TeamControl tc = (TeamControl) data;
		return tc.isPermissionOwner(userId);
	}

}
