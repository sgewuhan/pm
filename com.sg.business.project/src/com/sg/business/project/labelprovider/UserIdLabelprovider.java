package com.sg.business.project.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.User;

public class UserIdLabelprovider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		String userid = (String)element;
		User user = User.getUserById(userid);
		return user.getLabel();
	}
}
