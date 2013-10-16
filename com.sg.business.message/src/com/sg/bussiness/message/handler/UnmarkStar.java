package com.sg.bussiness.message.handler;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Message;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class UnmarkStar extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {

		Iterator<?> iter = selection.iterator();
		while (iter.hasNext()) {
			Object sel = iter.next();
			if (sel instanceof Message) {
				Message message = (Message) sel;
				try {
					message.doMarkStar(new CurrentAccountContext(),
							Boolean.FALSE);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
			}
		}
		vc.getViewer().setSelection(new StructuredSelection(new Object[] {}));

	}

}
