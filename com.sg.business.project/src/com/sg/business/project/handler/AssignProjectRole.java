package com.sg.business.project.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.User;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class AssignProjectRole extends AbstractNavigatorHandler {

	private static final String TITLE = "指派成员";

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final Shell shell = HandlerUtil.getActiveShell(event);
		final IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (!(selected instanceof ProjectRole)) {
			MessageUtil
					.showToast(shell, TITLE, "只能对项目角色指派成员", SWT.ICON_WARNING);
			return;
		}

		final ProjectRole rd = ((ProjectRole) selected);
		if (rd.isOrganizatioRole()) {
			MessageUtil
					.showToast(shell, TITLE, "只能对项目角色指派成员", SWT.ICON_WARNING);
			return;
		}

		final Project project = rd.getProject();

		Organization org = project.getFunctionOrganization();

		final ViewerControl vc = getCurrentViewerControl(event);
		// 显示用户选择器
		// 可选择项目所属职能部门的及下级部门的所有成员
		NavigatorSelector ns = new NavigatorSelector("organization.alluser") {
			@SuppressWarnings("unchecked")
			@Override
			protected void doOK(IStructuredSelection is) {
				try {
					rd.doAssignUsers(is.toList(), new CurrentAccountContext());
					vc.getViewer().refresh(rd);
					vc.expandItem(rd);

					// 4. 将更改消息传递到编辑器
					if (part instanceof INavigatorActionListener) {
						sendNavigatorActionEvent(
								(INavigatorActionListener) part,
								INavigatorActionListener.CREATE, new Integer(
										INavigatorActionListener.REFRESH));
					}
				} catch (Exception e) {
					MessageUtil.showToast(TITLE, e);
				}
				super.doOK(is);
			}

			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				return super.isSelectEnabled(is)
						&& (is.getFirstElement() instanceof User);
			}

		};
		ns.setMaster(org);
		ns.show();

	}

}
