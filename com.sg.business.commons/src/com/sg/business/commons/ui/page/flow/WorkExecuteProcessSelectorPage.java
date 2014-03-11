package com.sg.business.commons.ui.page.flow;

import com.sg.business.commons.ui.flow.ProcessSettingPanel;
import com.sg.business.model.Work;

public class WorkExecuteProcessSelectorPage extends AbstractWorkProcessPage {

	@Override
	protected String getProcessKey() {
		return Work.F_WF_EXECUTE;
	}
	
	@Override
	protected int getProcessSettingControl() {
		return ProcessSettingPanel.PROCESS_SELECTOR
				| ProcessSettingPanel.ROLE_SELECTOR|ProcessSettingPanel.ACTOR_SELECTOR;
	}

}
