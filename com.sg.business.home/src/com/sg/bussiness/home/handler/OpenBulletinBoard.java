package com.sg.bussiness.home.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.BulletinBoard;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;

public class OpenBulletinBoard extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof BulletinBoard) {
			BulletinBoard bulletinboard = (BulletinBoard) selected;
			if (bulletinboard.isReply()) {

				try {
					DataObjectEditor.open(bulletinboard,
							BulletinBoard.EDITOR_REPLY, false, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					DataObjectEditor.open(bulletinboard,
							BulletinBoard.EDITOR_CREATE, false, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
