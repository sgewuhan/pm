package com.sg.bussiness.home.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.BulletinBoard;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;

/**
 * 打开按钮点击事件
 * @author gdiyang
 *
 */
public class OpenBulletinBoard extends AbstractNavigatorHandler {

	/**
	 * 点击打开按钮执行事件
	 */
	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		//判断当前选择是否为公告板
		if (selected instanceof BulletinBoard) {
			//获取当前选择的公告板
			BulletinBoard bulletinboard = (BulletinBoard) selected;
			//依据是否是回复信息打开不同的公告板
			if (bulletinboard.isReply()) {
				//打开回复公告板，且不能进行编辑
				try {
					DataObjectEditor.open(bulletinboard,
							BulletinBoard.EDITOR_REPLY, false, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				//打开公告公告板，且不能进行编辑
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
