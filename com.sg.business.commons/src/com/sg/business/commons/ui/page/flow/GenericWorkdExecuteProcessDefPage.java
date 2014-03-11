package com.sg.business.commons.ui.page.flow;

import com.sg.business.commons.ui.flow.ProcessSettingPanel;
import com.sg.business.model.WorkDefinition;

public class GenericWorkdExecuteProcessDefPage extends AbstractWorkdProcessPage{

	@Override
	protected String getProcessKey() {
		return WorkDefinition.F_WF_EXECUTE;
	}
		
	@Override
	protected int getProcessSettingControl() {
		return ProcessSettingPanel.PROCESS_SELECTOR;
	}

}