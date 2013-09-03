package com.sg.bussiness.message.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Message;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;

public class ReplyMessage extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {

		if (selected instanceof Message) {
			Message message = (Message) selected;
			Message reply = message.makeReply();
			ViewerControl vc = getCurrentViewerControl(event);
			reply.setParentPrimaryObject(selected);
			reply.addEventListener(vc);
			try {
				DataObjectEditor.open(reply, Message.EDITOR_REPLY, true, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
