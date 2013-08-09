package com.sg.business.management.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.business.model.WorkDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.viewer.ViewerControl;

public class ShowWorkDefinitionCompase extends AbstractWorkDefinitionHandler {

	@Override
	protected void execute(WorkDefinition selected, ExecutionEvent event) {
		ViewerControl vc = getCurrentViewerControl(event);
		WorkDefinitionNavigatorPanel navi = new WorkDefinitionNavigatorPanel();
		
		navi.setViewerControl(vc);
		
		Tree tree = (Tree) vc.getViewer().getControl();
		Point point = tree.toDisplay(tree.getSize().x/2, tree.getSize().y/2);
		navi.activeNavigator(point);
	}

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageUtil.showToast(shell, "移动工作定义", "您需要选择一个工作定义", SWT.ICON_WARNING);
		return false;
	}
}
