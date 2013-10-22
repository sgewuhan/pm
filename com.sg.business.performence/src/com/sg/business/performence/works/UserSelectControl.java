package com.sg.business.performence.works;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.sg.business.model.User;
import com.sg.widgets.part.editor.fields.INavigatorSelectorControl;

public class UserSelectControl implements INavigatorSelectorControl {

	@Override
	public boolean isSelectEnable(IStructuredSelection is) {
		// 如果选择的进行中的工作，才能进行补报
		if (is == null || is.isEmpty()) {
			return false;
		}
		Object element = is.getFirstElement();
		return element instanceof User;
	}

}
