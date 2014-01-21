package com.sg.business.project.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Document;
import com.sg.business.model.IDeliverable;
import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class CreateDeliverableWithVault extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			final ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		final Work work = (Work) selected;

		NavigatorSelector ns = new NavigatorSelector("vault.document.selector") { //$NON-NLS-1$

			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				if (is != null && !is.isEmpty()) {
					Object o = is.getFirstElement();
					return o instanceof Document;
				}
				return false;
			}

			@Override
			protected void doOK(IStructuredSelection is) {
				if (is != null && !is.isEmpty()) {
					try {
						Object o = is.getFirstElement();
						if (o instanceof Document) {
							Document document = (Document) o;
							work.doAddDeliverable(document,
									IDeliverable.TYPE_LINK,
									new CurrentAccountContext());
							vc.getViewer().refresh(work, true);
						} else {
							MessageUtil.showToast(Messages.get().CreateDeliverableWithVault_1,
									SWT.ICON_WARNING);
						}
						super.doOK(is);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}

				} else {
					MessageUtil.showToast(Messages.get().CreateDeliverableWithVault_2, SWT.ICON_WARNING);
				}
			}
		};

		ns.show();
	}

}
