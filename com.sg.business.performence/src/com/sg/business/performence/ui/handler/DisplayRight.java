package com.sg.business.performence.ui.handler;

import com.sg.business.performence.ui.calendar.ResourceCalender;


public class DisplayRight extends AbstractCalendarHandler {

	@Override
	protected void execute(ResourceCalender part) {
		part.setDisplayNext();
	}


}
