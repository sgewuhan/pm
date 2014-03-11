package com.sg.business.commons.operation.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class CreateWorkFromGeneric extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast(Messages.get().CreateWorkFromGeneric_0, SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected,final IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {

		final ColumnViewer viewer = vc.getViewer();
		final Work work = ((Work) selected);
		Project project = work.getProject();
		NavigatorSelector nav = new NavigatorSelector(
				"management.genericwork.definitions", Messages.get().CreateWorkFromGeneric_2) { //$NON-NLS-1$

			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				if (!super.isSelectEnabled(is)) {
					return false;
				}
				WorkDefinition workd = (WorkDefinition) is.getFirstElement();
				return workd.isActivated();

			}

			@Override
			protected void doOK(IStructuredSelection is) {
				WorkDefinition workd = (WorkDefinition) is.getFirstElement();
				try {
					work.doCreateChildFromGenericWorkDefinition(workd,
							new CurrentAccountContext());
					viewer.refresh(work);
					sendNavigatorActionEvent(part,
							INavigatorActionListener.CUSTOMER, new Integer(
									INavigatorActionListener.REFRESH));
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
				super.doOK(is);
			}

		};
		nav.setMaster(project.getFunctionOrganization());
		nav.show();

	}

}
