package com.sg.business.commons.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.resource.nls.Messages;

public class RoleTypeLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if(element instanceof Role){
			return ((Role)element).getRoleTypeText();
		}else if(element instanceof RoleAssignment){
			return Messages.get().RoleTypeLabelProvider_0;
		}
		return ""; //$NON-NLS-1$
	}


}
