package com.sg.business.management.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class RemoveRoleDefinition extends AbstractNavigatorHandler {
	private static final String TITLE = "删除角色定义";

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageUtil.showToast(shell, TITLE, "您需要选择一个角色定义", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final IWorkbenchPart part = HandlerUtil.getActivePart(event);

		Shell shell = HandlerUtil.getActiveShell(event);
		
		if(selected instanceof AbstractRoleDefinition){
			AbstractRoleDefinition rd = (AbstractRoleDefinition) selected;
			if(rd.isSystemRole()){
				MessageUtil.showToast("您不能删除系统角色", SWT.ICON_WARNING);
				return;
			}
		}
		
		int yes = MessageUtil.showMessage(shell, TITLE,
				"您确定要删除这个角色定义吗？\n该操作将不可恢复，选择YES确认删除。", SWT.YES | SWT.NO
						| SWT.ICON_QUESTION);
		if(yes!=SWT.YES){
			return;
		}
		
		
		ViewerControl vc = getCurrentViewerControl(event);
		selected.addEventListener(vc);

		try {
			selected.doRemove(new CurrentAccountContext());
			if (part instanceof INavigatorActionListener) {
				//通知编辑器发生了更改，侦听编辑器动作的页面可以进行响应
				sendNavigatorActionEvent(
						(INavigatorActionListener) part,
						INavigatorActionListener.CREATE,
						new Integer(
								INavigatorActionListener.REFRESH));
			}
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(),
					SWT.ICON_WARNING);
		}
	}

}
