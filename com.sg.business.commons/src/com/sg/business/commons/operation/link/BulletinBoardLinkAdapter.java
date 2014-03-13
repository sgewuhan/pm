package com.sg.business.commons.operation.link;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.BulletinBoard;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.editor.DataObjectDialog;

public class BulletinBoardLinkAdapter implements SelectionListener {

	public BulletinBoardLinkAdapter() {
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		if (event.detail == RWT.HYPERLINK) {
			try {
				String action = event.text.substring(
						event.text.lastIndexOf("/") + 1, //$NON-NLS-1$
						event.text.indexOf("@")); //$NON-NLS-1$
				String _data = event.text
						.substring(event.text.indexOf("@") + 1); //$NON-NLS-1$
				if ("replybulletinboard".equals(action)) { //$NON-NLS-1$
					replyBulletinBoard(_data, event);
				}
			} catch (Exception e) {
			}
		}
	}

	private void replyBulletinBoard(String _data, SelectionEvent event) {
		BulletinBoard bulletinBoard = ModelService.createModelObject(
				BulletinBoard.class, new ObjectId(_data));
		BulletinBoard reply = bulletinBoard.makeReply(null);
		try {
			DataObjectDialog.openDialog(reply, BulletinBoard.EDITOR_CREATE,
					true, null);
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

	}

}
