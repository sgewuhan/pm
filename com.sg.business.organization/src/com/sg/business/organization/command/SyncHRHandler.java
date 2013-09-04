package com.sg.business.organization.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.widgets.part.IRefreshablePart;

public class SyncHRHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		long start = System.currentTimeMillis();

		// 与HR的组织进行同步
		SyscHR syscHR = new SyscHR();
		syscHR.doSyscHROrganization();

		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (part instanceof IRefreshablePart) {
			((IRefreshablePart) part).doRefresh();
		}
		System.out.println(System.currentTimeMillis() - start);
		return null;
	}

}
