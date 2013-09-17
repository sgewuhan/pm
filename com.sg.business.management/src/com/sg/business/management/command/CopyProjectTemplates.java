package com.sg.business.management.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class CopyProjectTemplates extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		if(!(selected instanceof Organization)){
			MessageUtil.showToast(shell, "复制项目模版", "只能在组织中复制项目模版", SWT.ICON_WARNING);
			return;
		}

		final Organization po = (Organization) selected;
		ViewerControl currentViewerControl = getCurrentViewerControl(event);
		Assert.isNotNull(currentViewerControl);

		final ViewerControl vc = getCurrentViewerControl(event);
		NavigatorSelector ns = new NavigatorSelector("management.copyprojecttemplates") {
			@SuppressWarnings("unchecked")
			@Override
			protected void doOK(IStructuredSelection is) {
				//po.doAssignUsers(is.toList(),new CurrentAccountContext());
				po.doCopyProjectTemplates(is.toList(),new CurrentAccountContext());
				vc.getViewer().refresh(po);
				super.doOK(is);
			}

			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				return super.isSelectEnabled(is)
						&& (is.getFirstElement() instanceof User);
			}

		};

		ns.show();
	}

}
