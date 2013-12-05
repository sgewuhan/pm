package com.tmt.pdm.dcpdm.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class LinkPDMDocAndDraw extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		String userId = new CurrentAccountContext().getConsignerId();

		Shell shell = part.getSite().getShell();
		boolean result;
		try {
			result = DCPDMUtil.createDocumentFromDCPDM(userId,(Work) selected, shell);
			if (result) {
				vc.doRefresh();
			}
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

}
