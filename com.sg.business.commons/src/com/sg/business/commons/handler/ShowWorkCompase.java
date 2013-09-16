package com.sg.business.commons.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class ShowWorkCompase extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, final ExecutionEvent event) {
		ViewerControl vc = getCurrentViewerControl(event);
		
		WorkNavigatorPanel navi = new WorkNavigatorPanel(){
			@Override
			protected void rightPressed() {
				super.rightPressed();
				sendRefresh(event);
			}
			@Override
			protected void upPressed() {
				super.upPressed();
				sendRefresh(event);
			}
			
			@Override
			protected void leftPressed() {
				super.leftPressed();
				sendRefresh(event);
			}
			
			@Override
			protected void downPressed() {
				super.downPressed();
				sendRefresh(event);
			}
			
		};
		
		navi.setViewerControl(vc);
		
		Tree tree = (Tree) vc.getViewer().getControl();
		Point point = tree.toDisplay(tree.getSize().x/2, tree.getSize().y/2);
		navi.activeNavigator(point);
	}

	protected void sendRefresh(ExecutionEvent event) {
		// 将更改消息传递到编辑器
		sendNavigatorActionEvent(event, INavigatorActionListener.CUSTOMER,
				new Integer(INavigatorActionListener.REFRESH));		
	}

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageUtil.showToast(shell, "移动工作定义", "您需要选择一项", SWT.ICON_WARNING);
		return false;
	}
}
