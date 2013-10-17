package com.sg.business.performence.resource;


public class DisplayLeft extends TeamResourceHandler {

	@Override
	protected void execute(ResourceCalender part) {
		part.setDisplayPrevious();
	}


}
