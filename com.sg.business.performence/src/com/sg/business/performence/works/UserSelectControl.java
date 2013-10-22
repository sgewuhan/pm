package com.sg.business.performence.works;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.sg.business.model.User;
import com.sg.widgets.part.editor.fields.INavigatorSelectorControl;

public class UserSelectControl implements INavigatorSelectorControl {

	@Override
	public boolean isSelectEnable(IStructuredSelection is) {
		// ���ѡ��Ľ����еĹ��������ܽ��в���
		if (is == null || is.isEmpty()) {
			return false;
		}
		Object element = is.getFirstElement();
		return element instanceof User;
	}

}
