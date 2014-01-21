package com.sg.business.management.handler;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.Role;
import com.sg.business.model.RoleDefinition;
import com.sg.business.model.WorkDefinition;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class LinkOrganizationRole extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, final IWorkbenchPart part,
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

						sendNavigatorActionEvent(
								(INavigatorActionListener) part,
								INavigatorActionListener.CREATE, new Integer(
										INavigatorActionListener.REFRESH));

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

		PrimaryObject master = vc.getMaster();
		RoleDefinition roled = null;
		if (master instanceof ProjectTemplate) {
			ProjectTemplate pt = (ProjectTemplate) master;
			if (pt.hasOrganizationRole(role)) {
				throw new Exception(Messages.get().LinkOrganizationRole_3);
			}
			roled = pt.makeOrganizationRole(role);
		} else {
			WorkDefinition workd = (WorkDefinition) master;
			if (workd.hasOrganizationRole(role)) {
				throw new Exception(Messages.get().LinkOrganizationRole_14);
			}
			roled = workd.makeOrganizationRole(role);
		}

		roled.addEventListener(vc);
		roled.doSave(new CurrentAccountContext());

	}

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}

}
