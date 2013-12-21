package com.sg.business.commons.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class RemovePrimaryObject extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast(Messages.get().RemovePrimaryObject_0, SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {
		Shell shell = part.getSite().getShell();

		int yes = MessageUtil.showMessage(shell, Messages.get().RemovePrimaryObject_1 + selected.getTypeName(),
				Messages.get().RemovePrimaryObject_2, SWT.YES | SWT.NO
						| SWT.ICON_QUESTION);
		if (yes != SWT.YES) {
			return;
		}

		selected.addEventListener(vc);

		try {
			selected.doRemove(new CurrentAccountContext());
		} catch (Exception e) {
			MessageUtil.showToast(shell, Messages.get().RemovePrimaryObject_3 + selected.getTypeName(),
					e.getMessage(), SWT.ICON_WARNING);
		}
	}

}
