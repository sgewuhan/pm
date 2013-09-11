package com.sg.bussiness.message.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Message;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;

public class AddMessage extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		 Message message = ModelService.createModelObject(Message.class);
		 ViewerControl vc = getCurrentViewerControl(event);
		 message.addEventListener(vc);
		 try {
				DataObjectEditor.open(message,Message.EDITOR_SEND, true, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
	}

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		return true;
	}
	

}
