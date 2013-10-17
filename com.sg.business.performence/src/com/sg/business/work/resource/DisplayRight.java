package com.sg.business.work.resource;


public class DisplayRight extends TeamResourceHandler {

	@Override
	protected void execute(ResourceCalender part) {
		part.setDisplayNext();
	}


}
