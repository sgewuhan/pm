package com.sg.business.project.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.ProjectRoleAssignment;
import com.sg.business.model.User;
import com.sg.business.project.page.TransferPageConfig;
import com.sg.business.project.page.TransferUsersWizard;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectWizard;
import com.sg.widgets.viewer.ViewerControl;

public class TransferUsers extends AbstractNavigatorHandler {

	
	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Project po = (Project)selected;
		
		try {
			TransferUsersWizard w = TransferUsersWizard.open(po);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		
//		if(!(selected instanceof Project)){
//			MessageUtil.showToast(shell, TITLE, "只能对项目角色移交用户工作", SWT.ICON_WARNING);
//			return;
//		}
//
//		final ProjectRoleAssignment pra = ((ProjectRoleAssignment) selected);
//		ProjectRole rd = pra.getProjectRole();
//		if (rd.isOrganizatioRole()) {
//			MessageUtil
//					.showToast(shell, TITLE, "只能对项目角色移交用户工作", SWT.ICON_WARNING);
//			return;
//		}
//		
//		final Project project = rd.getProject();
//
//		Organization org = project.getFunctionOrganization();
//
//		final ViewerControl vc = getCurrentViewerControl(event);
//		// 显示用户选择器
//		// 可选择项目所属职能部门的及下级部门的所有成员
//		NavigatorSelector ns = new NavigatorSelector("organization.alluser") {
//			@SuppressWarnings("unchecked")
//			@Override
//			protected void doOK(IStructuredSelection is) {
//				try {
//					pra.doTransferUsers(project,is.toList(),new CurrentAccountContext());
//					vc.getViewer().refresh(pra);
//					vc.expandItem(pra);
//				} catch (Exception e) {
//					MessageUtil.showToast(TITLE, e);
//				}
//				super.doOK(is);
//			}
//
//			@Override
//			protected boolean isSelectEnabled(IStructuredSelection is) {
//				return super.isSelectEnabled(is)
//						&& (is.getFirstElement() instanceof User);
//			}
//
//		};
//
//		ns.setMaster(org);
//		ns.show();
	}

}
