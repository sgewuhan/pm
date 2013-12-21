package com.sg.business.commons.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;

public class RoleNumberLabelProvider extends ColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		return ((PrimaryObject)element).getImage();
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Role) {
			return ((Role) element).getRoleNumber();
		} else if (element instanceof RoleAssignment) {
			return ((RoleAssignment) element).getUserid();
		}
		return ""; //$NON-NLS-1$
	}
}
