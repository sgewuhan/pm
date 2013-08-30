package com.sg.business.project.handler;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Deliverable;
import com.sg.business.model.DocumentDefinition;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class CreateDeliverableWithTemplate extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final Work work = (Work) selected;
		final ViewerControl vc = getCurrentViewerControl(event);

		NavigatorSelector ns = new NavigatorSelector(
				"management.documentdefinition") {
			@Override
			protected void doOK(IStructuredSelection is) {
				if (is != null && !is.isEmpty()) {
					try {
						Iterator<?> iter = is.iterator();
						while (iter.hasNext()) {
							DocumentDefinition next = (DocumentDefinition) iter.next();
							Deliverable po = work.makeDeliverableDefinition(next);
							po.setParentPrimaryObject(work);
							po.addEventListener(vc);
							po.doSave(new CurrentAccountContext());
						}
						super.doOK(is);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}

				} else {
					MessageUtil.showToast("请选择文档定义", SWT.ICON_WARNING);
				}
			}
		};

		Project project = work.getProject();
		ns.setMaster(project.getFunctionOrganization());
		ns.show();
	}

}
