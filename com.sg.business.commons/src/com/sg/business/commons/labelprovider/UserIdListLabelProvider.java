package com.sg.business.commons.labelprovider;

import java.util.List;

import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class UserIdListLabelProvider extends ConfiguratorColumnLabelProvider {

	@Override
	public String getText(Object element) {
		Object value = getValue(element);
		if(value instanceof String[]){
			String label = "";
			String[] userIdArray = (String[]) value;
			for (int i = 0; i < userIdArray.length; i++) {
				if(i!=0){
					label += ", ";
				}
				String userid = userIdArray[i];
				User user = UserToolkit.getUserById(userid);
				if(user!=null){
					label += user.getLabel();
				}
			}
			return label;
		}else if(value instanceof List<?>){
			String label = "";
			List<?> userIdArray = (List<?>) value;
			for (int i = 0; i < userIdArray.size(); i++) {
				if(i!=0){
					label += ", ";
				}
				String userid = (String) userIdArray.get(i);
				User user = UserToolkit.getUserById(userid);
				if(user!=null){
					label += user.getLabel();
				}			}
			return label;
		}
		return "";
	}

}
