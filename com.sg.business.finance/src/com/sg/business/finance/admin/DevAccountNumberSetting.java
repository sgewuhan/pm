package com.sg.business.finance.admin;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.mobnut.admin.IFunctionExcutable;

public class DevAccountNumberSetting implements IFunctionExcutable {

	@Override
	public void run() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		try {
			page.showView("commons.admin.devaccount");
		} catch (PartInitException e) {
		}
	}

}
