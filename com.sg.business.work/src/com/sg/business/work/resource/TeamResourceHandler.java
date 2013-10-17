package com.sg.business.work.resource;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class TeamResourceHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ResourceCalender part = (ResourceCalender) HandlerUtil.getActivePart(event);
		
		execute(part);
		return null;
	}

	protected abstract void execute(ResourceCalender part);

}
