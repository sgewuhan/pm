package com.sg.business.commons.ui.page.flow;

import com.sg.business.model.WorkDefinition;


public class StandloneWorkdChangeProcessDefPage extends AbstractWorkdProcessPage{

	@Override
	protected String getProcessKey() {
		return WorkDefinition.F_WF_CHANGE;
	}
		

}
