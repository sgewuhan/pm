package com.sg.business.project.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.RoleAssignment;
import com.sg.business.project.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class RemoveProjectRoleOrMember extends AbstractNavigatorHandler {

	private static final String TITLE = Messages.get().RemoveProjectRoleOrMember_0;

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		final Shell shell = part.getSite().getShell();
		MessageUtil.showToast(shell, TITLE, Messages.get().RemoveProjectRoleOrMember_1, SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		final Shell shell = part.getSite().getShell();

		if (selected instanceof RoleAssignment) {
			MessageUtil.showToast(shell, TITLE, Messages.get().RemoveProjectRoleOrMember_2,
					SWT.ICON_WARNING);
			return;
		} else if (selected instanceof ProjectRole) {
			// [bug:18]
			// 解决项目经理角色的问题
			ProjectRole projectRole = (ProjectRole) selected;
			if (projectRole.isSystemRole()) {
				MessageUtil.showToast(Messages.get().RemoveProjectRoleOrMember_3, SWT.ICON_WARNING);
				return;
			}

		}

		int yes = MessageUtil.showMessage(shell, TITLE,
				Messages.get().RemoveProjectRoleOrMember_4, SWT.YES | SWT.NO
						| SWT.ICON_QUESTION);
		if (yes != SWT.YES) {
			return;
		}

		selected.addEventListener(vc);

		try {
			selected.doRemove(new CurrentAccountContext());
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(),
					SWT.ICON_WARNING);
		}
	}


}
