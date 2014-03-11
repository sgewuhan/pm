package com.sg.business.commons.ui.page.flow;

import com.sg.business.model.Project;

public class ProjectChangeProcessPage extends AbstractProjectProcessPage {

	@Override
	protected String getProcessKey() {
		return Project.F_WF_CHANGE;
	}


}
