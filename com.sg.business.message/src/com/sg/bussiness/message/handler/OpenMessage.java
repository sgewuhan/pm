package com.sg.bussiness.message.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Message;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;

public class OpenMessage extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		if (selected instanceof Message) {
			Message message = (Message) selected;

			try {
				message.doMarkRead(new CurrentAccountContext(), Boolean.TRUE);
				vc.getViewer().update(selected, null);
				Object isHtmlBody = message.getValue(Message.F_ISHTMLBODY);
				if (isHtmlBody != null && isHtmlBody.equals(Boolean.TRUE)) {
					DataObjectEditor.open(message, Message.EDITOR_HTMLVIEW,
							false, null);
				} else {
					DataObjectEditor.open(message, Message.EDITOR_VIEW, false,
							null);
				}
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}

		}
	}

}
