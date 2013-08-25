package com.sg.business.commons.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.User;

public class UserIdLabelProviderInField extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if(element instanceof String){
			User user = User.getUserById((String) element);
			if(user!=null){
				return user.getLabel();
			}else{
				return "?";
			}
		}
		return "";
	}
}
