package com.sg.business.commons.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.BulletinBoard;
import com.sg.business.model.Project;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.IEditablePart;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.viewer.ViewerControl;

public class AddBulletinBoard extends AbstractNavigatorHandler {

	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {

		if (part instanceof IEditablePart) {
			Shell shell = part.getSite().getShell();

			String hasproject = (String) parameters
					.get("bulletinboard.hasproject"); //$NON-NLS-1$
			// 判断当前选择是否为公告板
			Project master = (Project) vc.getMaster();
			if ("true".equals(hasproject)) { //$NON-NLS-1$
				BulletinBoard bulletinboard = master.makeBulletinBoard(null);
				bulletinboard.addEventListener(vc);
				try {
					DataObjectDialog.openDialog(bulletinboard,
							BulletinBoard.EDITOR_CREATE, true, null);

				} catch (Exception e) {
					MessageUtil.showToast(shell, Messages.get().AddBulletinBoard_2, e.getMessage(),
							SWT.ICON_ERROR);
				}
			} else {
				BulletinBoard bulletinboard = ModelService.createModelObject(
						new BasicDBObject(), BulletinBoard.class);
				bulletinboard.addEventListener(vc);
				try {
					DataObjectDialog.openDialog(bulletinboard,
							BulletinBoard.EDITOR_CREATE, true, null);
				} catch (Exception e) {
					MessageUtil.showToast(shell, Messages.get().AddBulletinBoard_3, e.getMessage(),
							SWT.ICON_ERROR);
				}
			}

		}
	}

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}

}
