package com.sg.business.management.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.management.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class RemoveDeliverable extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast(Messages.get().RemoveDeliverable_0, SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		Shell shell = part.getSite().getShell();
		int yes = MessageUtil
				.showMessage(shell, Messages.get().RemoveDeliverable_1 + selected.getTypeName(), Messages.get().RemoveDeliverable_2
						+ selected.getTypeName() + Messages.get().RemoveDeliverable_3,
						SWT.YES | SWT.NO | SWT.ICON_QUESTION);
		if (yes != SWT.YES) {
			return;
		}

		Assert.isNotNull(currentViewerControl);

		selected.addEventListener(currentViewerControl);
		try {
			selected.doRemove(new CurrentAccountContext());
		} catch (Exception e) {
			MessageUtil.showMessage(shell, Messages.get().RemoveDeliverable_4 + selected.getTypeName(),
					e.getMessage(), SWT.ICON_WARNING);
		}
		selected.removeEventListener(currentViewerControl);
	}

}
