package com.sg.business.work.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.business.work.view.ResourceWorksCalender;

public abstract class TeamResourceHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ResourceWorksCalender part = (ResourceWorksCalender) HandlerUtil.getActivePart(event);
		
		execute(part);
		return null;
	}

	protected abstract void execute(ResourceWorksCalender part);

}
