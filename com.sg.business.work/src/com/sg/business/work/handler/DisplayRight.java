package com.sg.business.work.handler;

import com.sg.business.work.resource.OrgResCalender;

public class DisplayRight extends TeamResourceHandler {

	@Override
	protected void execute(OrgResCalender part) {
		part.setDisplayNext();
	}


}
