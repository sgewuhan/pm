package com.sg.business.vault.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.business.vault.model.DocumentInputDialog;
import com.sg.widgets.part.view.AccountSensitiveTreeView;

public class QueryDocument extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		DocumentInputDialog inputDialg = new DocumentInputDialog(
				HandlerUtil.getActiveShell(event));
		int ret = inputDialg.open();
		if (ret == DocumentInputDialog.OK) { // get view part
			IWorkbenchPart part = HandlerUtil.getActivePart(event);
			if (part instanceof AccountSensitiveTreeView) {
				AccountSensitiveTreeView view = (AccountSensitiveTreeView) part;
				view.doRefresh();
			}
		}
		System.out.println("ok");
		return inputDialg;
		
	}

}
