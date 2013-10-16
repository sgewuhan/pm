package com.sg.business.work.handler;

import com.sg.business.work.view.ResourceWorksCalender;

public class DisplayLeft extends TeamResourceHandler {

	@Override
	protected void execute(ResourceWorksCalender part) {
		part.setDisplayPrevious();
	}


}
