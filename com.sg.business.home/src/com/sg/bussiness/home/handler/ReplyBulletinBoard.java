package com.sg.bussiness.home.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.BulletinBoard;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;

public class ReplyBulletinBoard extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof BulletinBoard) {
			BulletinBoard bulletinboard = (BulletinBoard) selected;
			BulletinBoard reply = ModelService.createModelObject(BulletinBoard.class);
			reply.setValue(BulletinBoard.F_PARENT_BULLETIN, bulletinboard.get_id());
			ViewerControl vc = getCurrentViewerControl(event);
			reply.setParentPrimaryObject(selected);
			reply.addEventListener(vc);
			try {
				DataObjectEditor.open(reply, BulletinBoard.EDITOR_REPLY, true, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
