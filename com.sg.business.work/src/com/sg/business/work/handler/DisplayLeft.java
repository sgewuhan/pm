package com.sg.business.work.handler;

import com.sg.business.work.view.DeptWork;

public class DisplayLeft extends TeamResourceHandler {

	@Override
	protected void execute(DeptWork part) {
		part.setDisplayPrevious();
	}


}
