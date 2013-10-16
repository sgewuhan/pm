package com.sg.business.work.handler;

import com.sg.business.work.view.OrgResCalender;

public class DisplayLeft extends TeamResourceHandler {

	@Override
	protected void execute(OrgResCalender part) {
		part.setDisplayPrevious();
	}


}
