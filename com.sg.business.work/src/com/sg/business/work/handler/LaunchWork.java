package com.sg.business.work.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.sg.business.work.launch.LaunchWorkWizard;

public class LaunchWork extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		LaunchWorkWizard.OPEN();
		
		return null;
	}

	
}
