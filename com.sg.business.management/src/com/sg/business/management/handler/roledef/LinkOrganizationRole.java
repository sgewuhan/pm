package com.sg.business.management.handler.roledef;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.Role;
import com.sg.business.model.RoleDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class LinkOrganizationRole extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final ViewerControl vc = getCurrentViewerControl(event);
		NavigatorSelector n = new NavigatorSelector("management.roleselector") {
			@Override
			protected void doOK(IStructuredSelection is) {
				if (is != null && !is.isEmpty()
						&& is.getFirstElement() instanceof Role) {
					doLinkOrganizationRole(vc,(Role)is.getFirstElement());
					
					super.doOK(is);
				}else{
					MessageUtil.showToast("ÇëÑ¡Ôñ½ÇÉ«", SWT.ICON_WARNING);
				}
			}
		};
		n.show();
	}

	private void doLinkOrganizationRole(ViewerControl vc, Role role) {
		ProjectTemplate master = (ProjectTemplate) vc.getMaster();
		try {
			RoleDefinition roled = master.makeOrganizationRole(role);
			roled.addEventListener(vc);
			roled.doSave(new CurrentAccountContext());
		} catch (Exception e) {
			MessageUtil.showToast(e.getMessage(), SWT.ICON_WARNING);
		}
		
	}

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		return true;
	}

}
