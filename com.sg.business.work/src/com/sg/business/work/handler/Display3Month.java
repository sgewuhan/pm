package com.sg.business.work.handler;

import com.sg.business.work.resource.OrgResCalender;

public class Display3Month extends TeamResourceHandler {

	@Override
	protected void execute(OrgResCalender part) {
		part.setDisplay3Month();
	}


}
