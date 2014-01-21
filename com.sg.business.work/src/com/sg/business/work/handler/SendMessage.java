package com.sg.business.work.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Message;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.MessageToolkit;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class SendMessage extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selected instanceof Work) {
			Work work = (Work) selected;
			Map<String, Message> senderMessage = getSenderMessage(work,
					new CurrentAccountContext());
			Iterator<Message> iter = senderMessage.values().iterator();
			while (iter.hasNext()) {
				Message message = iter.next();
				try {
					message.doSave(new CurrentAccountContext());
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
			}

		}
	}

	@SuppressWarnings("rawtypes")
	private Map<String, Message> getSenderMessage(Work work, IContext context) {

		Map<String, Message> messageList = new HashMap<String, Message>();
		String title = Messages.get().SendMessage_0;

		// 添加消息：负责人
		MessageToolkit.appendMessage(messageList, work.getChargerId(), title,
				Messages.get().SendMessage_1 + ": " + work.getLabel(), work, Work.EDITOR, context); //$NON-NLS-2$

		// 添加消息：参与者
		List participatesidlist = work.getParticipatesIdList();
		if (participatesidlist != null) {

			for (Object id : participatesidlist) {
				MessageToolkit.appendMessage(messageList, (String) id, title,
						Messages.get().SendMessage_3 + ": " + work.getLabel(), work, Work.EDITOR, //$NON-NLS-2$
						context);
			}
		}

		// 添加消息：指派者
		String consignerId = context.getAccountInfo().getConsignerId();
		String assignerId = work.getAssignerId();
		if (!consignerId.equals(assignerId)) {
			MessageToolkit.appendMessage(messageList, work.getAssignerId(),
					title, Messages.get().SendMessage_5 + ": " + work.getLabel(), work, //$NON-NLS-2$
					Work.EDITOR, context);
		}

		List<PrimaryObject> children = work.getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childwork = (Work) children.get(i);
			childwork.getCommitMessage(messageList, title, context);
		}
		return messageList;
	}
}
