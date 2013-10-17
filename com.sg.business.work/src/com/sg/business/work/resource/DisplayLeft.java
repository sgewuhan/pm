package com.sg.business.work.resource;


public class DisplayLeft extends TeamResourceHandler {

	@Override
	protected void execute(ResourceCalender part) {
		part.setDisplayPrevious();
	}


}
