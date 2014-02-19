package com.sg.business.pm2.home.widget;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.sg.widgets.MessageUtil;
import com.sg.widgets.block.Block;

public class DocBlock extends Block {

	private static final String PERSPECTIVE = "perspective.vault";
	public DocBlock(Composite parent) {
		super(parent);
	}
	@Override
	protected void go() {
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			workbench.showPerspective(PERSPECTIVE, window);
		} catch (WorkbenchException e) {
			MessageUtil.showToast(e);
		}
	}
}
