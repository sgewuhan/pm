package com.sg.bussiness.message.handler;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Message;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.CTableViewer;
import com.sg.widgets.viewer.ViewerControl;

public class RestoreMessage extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		IStructuredSelection ssel = (IStructuredSelection) vc.getViewer()
				.getSelection();
		Iterator<?> iter = ssel.iterator();
		while (iter.hasNext()) {
			Object sel = iter.next();
			if (sel instanceof Message) {
				Message message = (Message) sel;
				try {
					message.doRestore(new CurrentAccountContext(),
							Boolean.FALSE);
					CTableViewer viewer = (CTableViewer) vc.getViewer();
					viewer.remove(message);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
			}
		}

		// 刷新收件箱
		String viewId = "message.recieved";
		Widgets.refreshNavigatorView(viewId);

		// 刷新发件箱
		viewId = "message.send";
		Widgets.refreshNavigatorView(viewId);
	}

}
