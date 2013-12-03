package com.sg.business.visualization;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.widgets.jofc.JOFCDemo;

public class CheckFlash extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event);
		JOFCDemo d = new JOFCDemo(shell,SWT.SHELL_TRIM);
		d.doLoadPie();
		d.open();
		return null;
	}

}
