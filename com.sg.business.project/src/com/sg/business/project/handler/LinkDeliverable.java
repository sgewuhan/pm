package com.sg.business.project.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class LinkDeliverable extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			final ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		final Work work = (Work) selected;

		NavigatorSelector ns = new NavigatorSelector(
				"project.deliverable.selector") { //$NON-NLS-1$

			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				if (is != null && !is.isEmpty()) {
					Object o = is.getFirstElement();
					return o instanceof Deliverable;
				}
				return false;
			}

			@Override
			protected void doOK(IStructuredSelection is) {
				if (is != null && !is.isEmpty()) {
					try {
						Object o = is.getFirstElement();
						if (o instanceof Deliverable) {
							Deliverable deliverable = (Deliverable) o;
							Document document = deliverable.getDocument();
							if (document != null) {
								work.doAddDeliverable(document,Deliverable.TYPE_REFERENCE,
										new CurrentAccountContext());
								try {
									vc.getViewer().refresh(work, true);
								} catch (Exception e) {
									UIFrameworkUtils.refreshHomePart(true);
								}
							} else {
								MessageUtil.showToast(Messages.get().LinkDeliverable_1,
										SWT.ICON_WARNING);
							}
						}
						super.doOK(is);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}

				} else {
					MessageUtil.showToast(Messages.get().LinkDeliverable_2, SWT.ICON_WARNING);
				}
			}
		};

		Project project = work.getProject();
		ns.setMaster(project);
		ns.show();
	}


}
