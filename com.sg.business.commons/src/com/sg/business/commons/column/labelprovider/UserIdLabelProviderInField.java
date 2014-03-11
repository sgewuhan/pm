package com.sg.business.commons.column.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;

public class UserIdLabelProviderInField extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if(element instanceof String){
			User user = UserToolkit.getUserById((String) element);
			if(user!=null){
				return user.getLabel();
			}else{
				return "?"; //$NON-NLS-1$
			}
		}
		return ""; //$NON-NLS-1$
	}
}
