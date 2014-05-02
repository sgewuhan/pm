package com.sg.business.performence.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.business.performence.ui.EmployeeWorksFromWork;

public class WorksCollapse extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (part instanceof EmployeeWorksFromWork) {
			EmployeeWorksFromWork view = (EmployeeWorksFromWork) part;
			view.collapse();
		}
		return null;
	}

}
