package com.sg.business.project.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class UnLockDocument extends AbstractNavigatorHandler {
	private static final String TITLE = Messages.get().UnLockDocument_0;

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selected instanceof Deliverable) {
			Shell shell = part.getSite().getShell();
			Deliverable deliverable = (Deliverable) selected;
			Document document = deliverable.getDocument();
			try {
				document.doUnLock(new CurrentAccountContext());
				vc.getViewer().refresh(selected);
				vc.getViewer().setSelection(null);
			} catch (Exception e) {
				MessageUtil.showToast(shell, TITLE, e.getMessage(), SWT.ICON_ERROR);
			}

		}
	}
}
