package com.sg.business.work.handler;

import com.sg.business.work.view.DeptWork;

public class Display3Month extends TeamResourceHandler {

	@Override
	protected void execute(DeptWork part) {
		part.setDisplay3Month();
	}


}
