package com.sg.business.commons.adminfunction;

import org.eclipse.core.commands.ExecutionException;

import com.mobnut.admin.IFunctionExcutable;
import com.sg.business.commons.ui.CheckJS;

public class CheckJSFunction implements IFunctionExcutable {

	public CheckJSFunction() {
	}

	@Override
	public void run() {
		try {
			new CheckJS().execute(null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}
