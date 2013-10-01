package com.sg.business.management.editor.page.process;

import com.sg.business.model.ProjectTemplate;

public class ProjectTemplateCommitProcessDefPage extends AbstractProjecttProcessSettingPage{

	@Override
	protected String getProcessKey() {
		return ProjectTemplate.F_WF_COMMIT;
	}

}
