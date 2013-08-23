package com.sg.business.project.action;

import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.NavigatorAction;

public class CheckPlan extends NavigatorAction {

	public CheckPlan() {
		setText("检查项目计划");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_CHECK_24));
	}

}
