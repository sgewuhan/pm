package com.sg.business.commons.column.editingsupport;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.User;

public class UserNavigatorSelectorEditing extends NavigatorSelectorEditing {

	public UserNavigatorSelectorEditing() {
	}

	@Override
	protected String getNavigatorId() {
		return "organization.user.selector"; //$NON-NLS-1$
	}

	@Override
	protected Object getValueFromSelection(IStructuredSelection is) {
		if (is == null || is.isEmpty()) {
			return null;
		}
		User user = (User) is.getFirstElement();
		return user.getUserid();
	}

	@Override
	protected boolean canEdit(Object element) {
		if (element instanceof PrimaryObject) {
			return !Boolean.FALSE.equals(((PrimaryObject) element)
					.getValue("ecn_canedit"));
		}
		return true;
	}
}
