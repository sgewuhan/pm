package com.sg.bussiness.message.handler;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Message;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class MarkStar extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {

		ViewerControl vc = getCurrentViewerControl(event);
		IStructuredSelection ssel = (IStructuredSelection) vc.getViewer()
				.getSelection();
		Iterator<?> iter = ssel.iterator();
		while (iter.hasNext()) {
			Object sel = iter.next();
			if (sel instanceof Message) {
				Message message = (Message) sel;
				try {
					message.doMarkStar(new CurrentAccountContext(),
							Boolean.TRUE);
					vc.getViewer().update(message, null);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
			}
		}
		vc.getViewer().setSelection(new StructuredSelection(new Object[] {}));

	}

}
