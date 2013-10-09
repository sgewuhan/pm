package com.sg.business.management.editor.page.process;

import com.sg.business.commons.ui.flow.ProcessSettingPanel2;
import com.sg.business.model.WorkDefinition;

public class ProjectWorkdExecuteProcessDefPage extends AbstractWorkdProcessSettingPage{

	@Override
	protected String getProcessKey() {
		return WorkDefinition.F_WF_EXECUTE;
	}
		
	@Override
	protected int getProcessSettingControl() {
		return ProcessSettingPanel2.PROCESS_SELECTOR|ProcessSettingPanel2.ROLE_SELECTOR;
	}

}