package com.sg.business.management.handler;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.DeliverableDefinition;
import com.sg.business.model.DocumentDefinition;
import com.sg.business.model.IDeliverable;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class LinkDeliverableDefinition extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			final ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		final WorkDefinition workd = (WorkDefinition) selected;

		NavigatorSelector ns = new NavigatorSelector(
				"management.documentdefinition") { //$NON-NLS-1$
			@Override
			protected void doOK(IStructuredSelection is) {
				if (is != null && !is.isEmpty()) {
					try {
						Iterator<?> iter = is.iterator();
						while (iter.hasNext()) {
							DocumentDefinition next = (DocumentDefinition) iter
									.next();
							DeliverableDefinition po = workd
									.makeDeliverableDefinition(next,IDeliverable.TYPE_REFERENCE);
							po.setParentPrimaryObject(workd);
							po.addEventListener(currentViewerControl);
							po.doSave(new CurrentAccountContext());
						}
						super.doOK(is);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}

				} else {
					MessageUtil.showToast(Messages.get().LinkDeliverableDefinition_1, SWT.ICON_WARNING);
				}
			}
		};

		Organization org = null;
		ProjectTemplate projectTemplate = workd.getProjectTemplate();
		if (projectTemplate != null) {
			org = projectTemplate.getOrganization();
		} else {
			org = workd.getOrganization();
		}
		ns.setMaster(org);
		ns.show();
	}

}
