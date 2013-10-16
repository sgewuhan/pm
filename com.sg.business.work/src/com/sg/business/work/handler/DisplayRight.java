package com.sg.business.work.handler;

import com.sg.business.work.view.OrgResCalender;

public class DisplayRight extends TeamResourceHandler {

	@Override
	protected void execute(OrgResCalender part) {
		part.setDisplayNext();
	}


}
