package com.sg.business.management.handler.workdef;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.RoleDefinition;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class ChargerAssignment extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final WorkDefinition workd = (WorkDefinition) selected;
		ViewerControl vc = getCurrentViewerControl(event);
		workd.addEventListener(vc);
		NavigatorSelector ns = new NavigatorSelector(
				"management.roledefinition") {
			@Override
			protected void doOK(IStructuredSelection is) {
				if (is != null && !is.isEmpty()) {
					try {
						Object next = is.getFirstElement();
						workd.doSetChargerAssignmentRole((RoleDefinition) next,
								new CurrentAccountContext());
						super.doOK(is);
					} catch (Exception e) {
						MessageUtil.showToast(e.getMessage(), SWT.ICON_WARNING);
					}

				} else {
					MessageUtil.showToast("请选择角色定义", SWT.ICON_WARNING);
				}
			}
		};
		ProjectTemplate projectTemplate = workd.getProjectTemplate();
		ns.setMaster(projectTemplate);
		ns.show();
	}

}
