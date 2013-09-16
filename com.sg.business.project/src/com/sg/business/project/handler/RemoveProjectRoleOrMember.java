package com.sg.business.project.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.RoleAssignment;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class RemoveProjectRoleOrMember extends AbstractNavigatorHandler {

	private static final String TITLE = "删除项目角色";

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageUtil.showToast(shell, TITLE, "您需要选择一个角色或用户指派", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);

		if (selected instanceof RoleAssignment) {
			MessageUtil.showToast(shell, TITLE, "组织上的角色指派需在组织管理中移除",
					SWT.ICON_WARNING);
			return;
		} else if (selected instanceof ProjectRole) {
			// [bug:18]
			// 解决项目经理角色的问题
			ProjectRole projectRole = (ProjectRole) selected;
			if (projectRole.isSystemRole()) {
				MessageUtil.showToast("您不能删除系统角色",SWT.ICON_WARNING);
				return;
			}

		}

		int yes = MessageUtil.showMessage(shell, TITLE,
				"您确定要删除吗？\n该操作将不可恢复，选择YES确认删除。", SWT.YES | SWT.NO
						| SWT.ICON_QUESTION);
		if (yes != SWT.YES) {
			return;
		}

		ViewerControl vc = getCurrentViewerControl(event);
		selected.addEventListener(vc);

		try {
			selected.doRemove(new CurrentAccountContext());
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(),
					SWT.ICON_WARNING);
		}
	}

}
