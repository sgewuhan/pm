package com.sg.business.commons.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;

public class RoleDescLabelProvider extends ColumnLabelProvider {


	@Override
	public String getText(Object element) {
		if (element instanceof Role) {
			return ((Role) element).getDesc();
		} else if (element instanceof RoleAssignment) {
			return ((RoleAssignment) element).getUsername();
		}
		return "";
	}

}
