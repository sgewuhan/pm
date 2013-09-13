package com.sg.business.commons.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.BulletinBoard;
import com.sg.business.model.Project;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.IEditablePart;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;

public class AddBulletinBoard extends AbstractNavigatorHandler {

	public void execute(PrimaryObject selected, ExecutionEvent event) {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (part instanceof IEditablePart) {
			String hasproject = event.getParameter("bulletinboard.hasproject");
			// 判断当前选择是否为公告板
			ViewerControl vc = getCurrentViewerControl(event);
			Project master = (Project) vc.getMaster();
			if ("true".equals(hasproject)) {
				BulletinBoard bulletinboard = master.makeBulletinBoard(null);
				bulletinboard.addEventListener(vc);
				try {
					DataObjectDialog.openDialog(bulletinboard,
							BulletinBoard.EDITOR_CREATE, true, null);

				} catch (Exception e) {
					MessageUtil.showToast(HandlerUtil.getActiveShell(event),
							"创建公告板", e.getMessage(), SWT.ICON_ERROR);
				}
			} else {
				BulletinBoard bulletinboard = ModelService.createModelObject(
						new BasicDBObject(), BulletinBoard.class);
				bulletinboard.addEventListener(vc);
				try {
					DataObjectEditor.open(bulletinboard,
							BulletinBoard.EDITOR_CREATE, true, null);
				} catch (Exception e) {
					MessageUtil.showToast(HandlerUtil.getActiveShell(event),
							"创建公告板", e.getMessage(), SWT.ICON_ERROR);
				}
			}

		}
	}

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		return true;
	}
}
