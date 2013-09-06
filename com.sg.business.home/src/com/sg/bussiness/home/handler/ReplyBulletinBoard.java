package com.sg.bussiness.home.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.BulletinBoard;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;

/**
 * 回复按钮点击事件
 * @author gdiyang
 *
 */
public class ReplyBulletinBoard extends AbstractNavigatorHandler {

	/**
	 * 点击回复按钮执行事件
	 */
	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		//判断当前选择是否为公告板
		if (selected instanceof BulletinBoard) {
			//获取当前选择的公告板
			BulletinBoard bulletinboard = (BulletinBoard) selected;
			//创建新公告板
			BulletinBoard reply = ModelService.createModelObject(BulletinBoard.class);
			//设置新公告板的上级公告ID
			reply.setValue(BulletinBoard.F_PARENT_BULLETIN, bulletinboard.get_id());
			ViewerControl vc = getCurrentViewerControl(event);
			reply.setParentPrimaryObject(selected);
			reply.addEventListener(vc);
			//使用回复编辑器打开新创建的公告板
			try {
				DataObjectEditor.open(reply, BulletinBoard.EDITOR_REPLY, true, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
