package com.sg.business.performence.ui.handler;

import com.sg.business.performence.ui.calendar.ResourceCalender;


public class DisplayYear extends AbstractCalendarHandler {

	@Override
	protected void execute(ResourceCalender part) {
		part.setDisplayYear();
	}


}
