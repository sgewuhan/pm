package com.sg.sales.editor;

import com.sg.business.commons.page.flow.AbstractWorkProcessPage;
import com.sg.business.commons.ui.flow.ProcessSettingPanel2;
import com.sg.business.model.Work;

public class PageApplyExpense extends AbstractWorkProcessPage {

	@Override
	protected String getProcessKey() {
		return Work.F_WF_EXECUTE;
	}

	@Override
	protected int getProcessSettingControl() {
		return ProcessSettingPanel2.ROLE_SELECTOR|ProcessSettingPanel2.ACTOR_SELECTOR;
	}
	
	@Override
	public boolean createBody() {
		return true;
	}
}