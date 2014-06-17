package com.sg.business.performence.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.business.performence.model.MonthInputDialog;
import com.sg.business.performence.ui.EmployeeWorksFromWork;

public class QueryWorks extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		MonthInputDialog inputDialog=new MonthInputDialog(HandlerUtil.getActiveShell(event));
		int ret = inputDialog.open();
		if(ret == MonthInputDialog.OK){
			int year = inputDialog.getYear();
			int month = inputDialog.getMonth();
			//get view part
			IWorkbenchPart part = HandlerUtil.getActivePart(event);
			if(part instanceof EmployeeWorksFromWork){
				EmployeeWorksFromWork view = (EmployeeWorksFromWork) part;
				view.setInput(year, month);
				view.doRefresh();
			}
		}
		return inputDialog;
	}

}
