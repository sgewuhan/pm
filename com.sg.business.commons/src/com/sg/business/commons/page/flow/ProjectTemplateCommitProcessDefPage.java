package com.sg.business.commons.page.flow;

import com.sg.business.model.ProjectTemplate;

public class ProjectTemplateCommitProcessDefPage extends AbstractProjectTemplateProcessPage{

	@Override
	protected String getProcessKey() {
		return ProjectTemplate.F_WF_COMMIT;
	}

}