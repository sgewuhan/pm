package com.sg.business.performence.handler;

import com.sg.business.performence.calendar.ResourceCalender;
import com.sg.business.performence.organization.TeamResourceHandler;


public class DisplayRight extends TeamResourceHandler {

	@Override
	protected void execute(ResourceCalender part) {
		part.setDisplayNext();
	}


}
