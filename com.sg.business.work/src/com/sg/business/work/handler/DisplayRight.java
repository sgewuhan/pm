package com.sg.business.work.handler;

import com.sg.business.work.view.DeptWork;

public class DisplayRight extends TeamResourceHandler {

	@Override
	protected void execute(DeptWork part) {
		part.setDisplayNext();
	}


}
