package com.sg.business.project.handler.role;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.User;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectRoleAssignment extends AbstractNavigatorHandler {

	private static final String TITLE = "ָ���û�";

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final Shell shell = HandlerUtil.getActiveShell(event);
		if(!(selected instanceof ProjectRole)){
			MessageUtil.showToast(shell, TITLE, "ֻ�ܶ���Ŀ��ɫָ���û�", SWT.ICON_WARNING);
			return;
		}

		final ProjectRole rd = ((ProjectRole) selected);
		if (rd.isOrganizatioRole()) {
			MessageUtil
					.showToast(shell, TITLE, "ֻ�ܶ���Ŀ��ɫָ���û�", SWT.ICON_WARNING);
			return;
		}

		Project project = rd.getProject();

		Organization org = project.getFunctionOrganization();

		final ViewerControl vc = getCurrentViewerControl(event);
		// ��ʾ�û�ѡ����
		// ��ѡ����Ŀ����ְ�ܲ��ŵļ��¼����ŵ����г�Ա
		NavigatorSelector ns = new NavigatorSelector("organization.alluser") {
			@Override
			protected void doOK(IStructuredSelection is) {
				try {
					rd.doAssignUsers(is.toList());
					vc.getViewer().refresh(rd);
					vc.expandItem(rd);
				} catch (Exception e) {
					MessageUtil
					.showToast(shell, TITLE, e.getMessage(), SWT.ICON_WARNING);
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
