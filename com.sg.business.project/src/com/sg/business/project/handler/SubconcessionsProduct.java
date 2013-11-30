package com.sg.business.project.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProductItem;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class SubconcessionsProduct extends AbstractNavigatorHandler {
	private static final String TITLE = "物资转批";

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selected instanceof ProductItem) {
			Shell shell = part.getSite().getShell();
			ProductItem productItem = (ProductItem) selected;
			try {
				productItem.doSubconcessions(new CurrentAccountContext());
				vc.getViewer().refresh(selected);
				vc.getViewer().setSelection(null);
			} catch (Exception e) {
				MessageUtil.showToast(shell, TITLE, e.getMessage(), SWT.ICON_ERROR);
			}

		}
	}
}
