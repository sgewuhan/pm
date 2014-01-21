package com.sg.bussiness.message.handler;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.sg.business.model.Message;
import com.sg.business.model.User;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;

public class SendToSelectedUser extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast(Messages.get().SendToSelectedUser_0, SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}
	
	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		Message message = ModelService.createModelObject(Message.class);
		BasicDBList recieverList = new BasicDBList();

		Iterator<?> iter = selection.iterator();
		while (iter.hasNext()) {
			User user = (User) iter.next();
			recieverList.add(user.getUserid());
		}

		message.setValue(Message.F_RECIEVER, recieverList);
		try {
			DataObjectEditor.open(message, Message.EDITOR_SEND, true, null);
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

}
