package com.sg.business.management.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class RemoveDeliverable extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast("您需要选择一项", SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		Shell shell = part.getSite().getShell();
		int yes = MessageUtil
				.showMessage(shell, "删除" + selected.getTypeName(), "您确定要删除这个"
						+ selected.getTypeName() + "吗？\n该操作将不可恢复，选择YES确认删除。",
						SWT.YES | SWT.NO | SWT.ICON_QUESTION);
		if (yes != SWT.YES) {
			return;
		}

		Assert.isNotNull(currentViewerControl);

		selected.addEventListener(currentViewerControl);
		try {
			selected.doRemove(new CurrentAccountContext());
		} catch (Exception e) {
			MessageUtil.showMessage(shell, "删除" + selected.getTypeName(),
					e.getMessage(), SWT.ICON_WARNING);
		}
		selected.removeEventListener(currentViewerControl);
	}

}
