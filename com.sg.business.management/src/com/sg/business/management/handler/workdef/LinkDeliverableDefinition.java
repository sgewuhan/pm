package com.sg.business.management.handler.workdef;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.DeliverableDefinition;
import com.sg.business.model.DocumentDefinition;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class LinkDeliverableDefinition extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final WorkDefinition workd = (WorkDefinition) selected;
		final ViewerControl vc = getCurrentViewerControl(event);

		NavigatorSelector ns = new NavigatorSelector(
				"management.projecttemplate.documents") {
			@Override
			protected void doOK(IStructuredSelection is) {
				if (is != null && !is.isEmpty()) {
					try {
						Iterator<?> iter = is.iterator();
						while (iter.hasNext()) {
							DocumentDefinition next = (DocumentDefinition) iter.next();
							DeliverableDefinition po = workd.makeDeliverableDefinition(next);
							po.setParentPrimaryObject(workd);
							po.addEventListener(vc);
							po.doSave(new CurrentAccountContext());
						}
						super.doOK(is);
					} catch (Exception e) {
						MessageUtil.showToast(e.getMessage(), SWT.ICON_WARNING);
					}

				} else {
					MessageUtil.showToast("请选择文档定义", SWT.ICON_WARNING);
				}
			}
		};

		ProjectTemplate projectTemplate = workd.getProjectTemplate();
		ns.setMaster(projectTemplate);
		ns.show();
	}

}
