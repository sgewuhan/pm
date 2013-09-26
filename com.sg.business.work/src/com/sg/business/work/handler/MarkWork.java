package com.sg.business.work.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class MarkWork extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof Work) {
			String userId = new CurrentAccountContext().getAccountInfo()
					.getConsignerId();
			boolean marked = ((Work) selected).isMarked(userId);
			try {
				((Work) selected).mark(userId, !marked);
				
				ViewerControl vc = getCurrentViewerControl(event);
				vc.getViewer().update(selected, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

}
