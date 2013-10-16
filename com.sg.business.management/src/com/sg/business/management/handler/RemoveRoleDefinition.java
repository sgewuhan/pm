package com.sg.business.management.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

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
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		Shell shell = part.getSite().getShell();
		MessageUtil.showToast(shell, TITLE, "您需要选择一个角色定义", SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {

		Shell shell = part.getSite().getShell();

		if (selected instanceof AbstractRoleDefinition) {
			AbstractRoleDefinition rd = (AbstractRoleDefinition) selected;
			if (rd.isSystemRole()) {
				MessageUtil.showToast("您不能删除系统角色", SWT.ICON_WARNING);
				return;
			}
		}

		int yes = MessageUtil.showMessage(shell, TITLE,
				"您确定要删除这个角色定义吗？\n该操作将不可恢复，选择YES确认删除。", SWT.YES | SWT.NO
						| SWT.ICON_QUESTION);
		if (yes != SWT.YES) {
			return;
		}

		selected.addEventListener(vc);

		try {
			selected.doRemove(new CurrentAccountContext());
			if (part instanceof INavigatorActionListener) {
				// 通知编辑器发生了更改，侦听编辑器动作的页面可以进行响应
				sendNavigatorActionEvent((INavigatorActionListener) part,
						INavigatorActionListener.CREATE, new Integer(
								INavigatorActionListener.REFRESH));
			}
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(),
					SWT.ICON_WARNING);
		}
	}

}
