package com.sg.business.commons.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.BulletinBoard;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;

/**
 * 打开按钮点击事件
 * 
 * @author gdiyang
 * 
 */
public class OpenBulletinBoard extends AbstractNavigatorHandler {

	/**
	 * 点击打开按钮执行事件
	 */
	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {
		// 判断当前选择是否为公告板
		if (selected instanceof BulletinBoard) {
			// 获取当前选择的公告板
			BulletinBoard bulletinboard = (BulletinBoard) selected;
			if (bulletinboard.getProjectId() != null) {
				try {
					DataObjectDialog.openDialog(bulletinboard,
							BulletinBoard.EDITOR_CREATE, true, null);
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
