package com.sg.business.project.editor.page;

import com.sg.business.model.Project;

public class ProjectChangeProcessPage extends AbstractProjectProcessSettingPage {

	@Override
	protected String getProcessKey() {
		return Project.F_WF_CHANGE;
	}


}
