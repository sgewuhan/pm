package com.sg.bussiness.message.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Message;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class MarkRead extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof Message) {
			Message message = (Message) selected;
			try {
				message.doMarkRead(new CurrentAccountContext(),Boolean.TRUE);
				ViewerControl vc = getCurrentViewerControl(event);
				vc.getViewer().update(selected, null);
				vc.getViewer().setSelection(new StructuredSelection(new Object[]{}));
			} catch (Exception e) {
				MessageUtil.showToast(e);
				e.printStackTrace();
			}
		}
	}

}
