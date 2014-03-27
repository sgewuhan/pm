package com.tmt.gs.field.control;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.sg.business.model.Organization;
import com.sg.widgets.part.editor.fields.INavigatorSelectorControl;

public class GSDeptSelectContorOfTB implements INavigatorSelectorControl {

	public GSDeptSelectContorOfTB() {
	}

	@Override
	public boolean isSelectEnable(IStructuredSelection is) {
		if (is == null || is.isEmpty()) {
			return false;
		}
		Object element = is.getFirstElement();
		if (element instanceof Organization) {
			Organization org =  (Organization)element;
			if (!org.isFunctionDepartment()) {
				return false;
			}
		}
		return true;
	}

}
