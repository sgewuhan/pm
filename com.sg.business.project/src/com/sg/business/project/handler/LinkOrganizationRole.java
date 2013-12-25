package com.sg.business.project.handler;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.Role;
import com.sg.business.project.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class LinkOrganizationRole extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			final ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		NavigatorSelector n = new NavigatorSelector("management.roleselector", //$NON-NLS-1$
				Messages.get().LinkOrganizationRole_1) {
			@Override
			protected void doOK(IStructuredSelection is) {
				if (is != null && !is.isEmpty()
						&& is.getFirstElement() instanceof Role) {
					try {
						Iterator<?> iter = is.iterator();
						while (iter.hasNext()) {
							Object next = iter.next();
							if (next instanceof Role) {
								doLinkOrganizationRole(vc, (Role) next);
							}
						}
						super.doOK(is);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}

				} else {
					MessageUtil.showToast(Messages.get().LinkOrganizationRole_2, SWT.ICON_WARNING);
				}
			}
		};
		n.show();
	}

	private void doLinkOrganizationRole(ViewerControl vc, Role role)
			throws Exception {
		Project master = (Project) vc.getMaster();

		if (master.hasOrganizationRole(role)) {
			throw new Exception(Messages.get().LinkOrganizationRole_3);
		}

		ProjectRole roled = master.makeOrganizationRole(role);
		roled.addEventListener(vc);
		roled.doSave(new CurrentAccountContext());

	}

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}


}
