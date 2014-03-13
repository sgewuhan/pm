package com.sg.business.commons.column.labelprovider;

import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class UserIdLabelProvider extends ConfiguratorColumnLabelProvider {

	@Override
	public String getText(Object element) {
		Object value = getValue(element);
		if(value instanceof String){
			User user = UserToolkit.getUserById((String) value);
			if(user!=null){
				return user.getLabel();
			}else{
				return "?"; //$NON-NLS-1$
			}
		}
		return ""; //$NON-NLS-1$
	}
}
