package com.sg.bussiness.message.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Message;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.viewer.ViewerControl;

public class ReplyMessage extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {

		if (selected instanceof Message) {
			Message message = (Message) selected;
			Message reply = message.makeReply();
			reply.setParentPrimaryObject(selected);
			reply.addEventListener(vc);
			try {
				DataObjectDialog.openDialog(reply, Message.EDITOR_REPLY, true,
						null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}

	}

}
