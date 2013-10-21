package com.sg.business.performence.handler;

import com.sg.business.performence.calendar.ResourceCalender;
import com.sg.business.performence.organization.TeamResourceHandler;


public class Display3Month extends TeamResourceHandler {

	@Override
	protected void execute(ResourceCalender part) {
		part.setDisplay3Month();
	}


}
