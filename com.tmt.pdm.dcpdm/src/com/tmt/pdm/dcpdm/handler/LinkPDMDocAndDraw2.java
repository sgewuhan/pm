package com.tmt.pdm.dcpdm.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IWorkRelative;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;
import com.tmt.pdm.dcpdm.nls.Messages;

public class LinkPDMDocAndDraw2 extends AbstractNavigatorHandler {


	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}

	@Override
	protected void execute(PrimaryObject po, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection se) {
		final Shell shell = part.getSite().getShell();

		final PrimaryObject master = vc.getMaster();
		Work work = getWork(master);
		if (work == null) {
			MessageUtil.showToast(shell, Messages.get().LinkPDMDocAndDraw2_0, Messages.get().LinkPDMDocAndDraw2_1, SWT.ICON_ERROR);
			return;
		}
		String userId = new CurrentAccountContext().getConsignerId();
		
		boolean result;
		try {
			result = DCPDMUtil.createDocumentFromDCPDM(userId,(Work) work, shell);
			if (result) {
				vc.doReloadData();
			}
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

	protected Work getWork(PrimaryObject master) {
		if (master instanceof Work) {
			return (Work) master;
		} else if (master instanceof IWorkRelative) {
			IWorkRelative iWorkRelative = (IWorkRelative) master;
			return (Work) iWorkRelative.getWork();
		} else {
			return null;
		}
	}

}
