package com.sg.business.commons.operation.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class ShowWorkCompase extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, final IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {

		WorkNavigatorPanel navi = new WorkNavigatorPanel() {
			@Override
			protected void rightPressed() {
				super.rightPressed();
				sendRefresh(part);
			}

			@Override
			protected void upPressed() {
				super.upPressed();
				sendRefresh(part);
			}

			@Override
			protected void leftPressed() {
				super.leftPressed();
				sendRefresh(part);
			}

			@Override
			protected void downPressed() {
				super.downPressed();
				sendRefresh(part);
			}

		};

		navi.setViewerControl(vc);

		Tree tree = (Tree) vc.getViewer().getControl();
		Point point = tree
				.toDisplay(tree.getSize().x / 2, tree.getSize().y / 2);
		navi.activeNavigator(point);
	}

	protected void sendRefresh(IWorkbenchPart part) {
		/**********************************************************
		 * [BUG:20] 将更改消息传递到编辑器
		 */
		sendNavigatorActionEvent(part, INavigatorActionListener.CUSTOMER,
				new Integer(INavigatorActionListener.REFRESH));
	}

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast(part.getSite().getShell(), Messages.get().ShowWorkCompase_0, Messages.get().ShowWorkCompase_1, SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}
}
