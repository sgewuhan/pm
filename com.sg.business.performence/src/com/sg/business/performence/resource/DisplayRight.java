package com.sg.business.performence.resource;


public class DisplayRight extends TeamResourceHandler {

	@Override
	protected void execute(ResourceCalender part) {
		part.setDisplayNext();
	}


}
