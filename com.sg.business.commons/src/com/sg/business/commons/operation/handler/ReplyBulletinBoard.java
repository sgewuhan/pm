package com.sg.business.commons.operation.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.BulletinBoard;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.viewer.ViewerControl;

/**
 * 回复按钮点击事件
 * 
 * @author gdiyang
 * 
 */
public class ReplyBulletinBoard extends AbstractNavigatorHandler {

	/**
	 * 点击回复按钮执行事件
	 */
	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {
		// 判断当前选择是否为公告板
		if (selected instanceof BulletinBoard) {
			// 获取当前选择的公告板
			BulletinBoard bulletinboard = (BulletinBoard) selected;
			// 创建新公告板
			BulletinBoard reply = bulletinboard.makeReply(null);

			reply.setParentPrimaryObject(selected);
			reply.addEventListener(vc);
			// 使用回复编辑器打开新创建的公告板
//			if (bulletinboard.getProjectId() != null) {
				try {
					DataObjectDialog.openDialog(reply,
							BulletinBoard.EDITOR_CREATE, true, null);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
//			} else {
//				try {
//					DataObjectDialog.openDialog(reply,
//							BulletinBoard.EDITOR_CREATE, true, null);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
		}

	}

}
