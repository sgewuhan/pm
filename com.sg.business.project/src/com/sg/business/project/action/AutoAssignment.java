package com.sg.business.project.action;

import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.NavigatorAction;

public class AutoAssignment extends NavigatorAction {

	public AutoAssignment() {
		setText("按角色自动指派");
		setImageDescriptor(BusinessResource.getImageDescriptor(BusinessResource.IMAGE_ASSIGNMENT_24));
	}

}
