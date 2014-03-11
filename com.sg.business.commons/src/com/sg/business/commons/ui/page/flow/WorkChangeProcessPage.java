package com.sg.business.commons.ui.page.flow;

import com.sg.business.commons.ui.flow.ProcessSettingPanel;
import com.sg.business.model.Work;

public class WorkChangeProcessPage extends AbstractWorkProcessPage {

	
	
	@Override
	protected String getProcessKey() {
		return Work.F_WF_CHANGE;
	}

	@Override
	protected int getProcessSettingControl() {
		return ProcessSettingPanel.ROLE_SELECTOR|ProcessSettingPanel.ACTOR_SELECTOR;
	}

}
