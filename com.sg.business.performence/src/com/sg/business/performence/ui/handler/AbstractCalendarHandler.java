package com.sg.business.performence.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.business.performence.ui.calendar.ResourceCalender;

public abstract class AbstractCalendarHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ResourceCalender part = (ResourceCalender) HandlerUtil.getActivePart(event);
		
		execute(part);
		return null;
	}

	protected abstract void execute(ResourceCalender part);

}
