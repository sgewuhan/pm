package com.sg.business.project.handler.role;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.Role;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class LinkOrganizationRole extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final ViewerControl vc = getCurrentViewerControl(event);
		NavigatorSelector n = new NavigatorSelector("management.roleselector",
				"ѡ����֯��ɫ") {
			@Override
			protected void doOK(IStructuredSelection is) {
				if (is != null && !is.isEmpty()
						&& is.getFirstElement() instanceof Role) {
					try {
						Iterator<?> iter = is.iterator();
						while (iter.hasNext()) {
							Object next = iter.next();
							if(next instanceof Role){
								doLinkOrganizationRole(vc, (Role) next);
							}
						}
						super.doOK(is);
					} catch (Exception e) {
						MessageUtil.showToast(e.getMessage(), SWT.ICON_WARNING);
					}

				} else {
					MessageUtil.showToast("��ѡ���ɫ", SWT.ICON_WARNING);
				}
			}
		};
		n.show();
	}

	private void doLinkOrganizationRole(ViewerControl vc, Role role)
			throws Exception {
		Project master = (Project) vc.getMaster();

		if (master.hasOrganizationRole(role)) {
			throw new Exception("�ý�ɫ�Ѿ�����");
		}

		ProjectRole roled = master.makeOrganizationRole(role);
		roled.addEventListener(vc);
		roled.doSave(new CurrentAccountContext());

	}

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		return true;
	}

}