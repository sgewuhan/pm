package com.sg.business.work.handler;

import com.sg.business.work.view.ResourceWorksCalender;

public class Display3Month extends TeamResourceHandler {

	@Override
	protected void execute(ResourceWorksCalender part) {
		part.setDisplay3Month();
	}


}
