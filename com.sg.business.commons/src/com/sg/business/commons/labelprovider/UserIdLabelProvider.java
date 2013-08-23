package com.sg.business.commons.labelprovider;

import com.sg.business.model.User;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class UserIdLabelProvider extends ConfiguratorColumnLabelProvider {

	@Override
	public String getText(Object element) {
		Object value = getValue(element);
		if(value instanceof String){
			User user = User.getUserById((String) value);
			if(user!=null){
				return user.getLabel();
			}else{
				return "?";
			}
		}
		return "";
	}
}
