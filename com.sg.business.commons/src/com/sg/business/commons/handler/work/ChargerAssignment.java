package com.sg.business.commons.handler.work;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractWork;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class ChargerAssignment extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final AbstractWork work = (AbstractWork) selected;
		ViewerControl vc = getCurrentViewerControl(event);
		work.addEventListener(vc);
		NavigatorSelector ns = new NavigatorSelector(
				getNavigatorId(work)) {
			@Override
			protected void doOK(IStructuredSelection is) {
				if (is != null && !is.isEmpty()) {
					try {
						Object next = is.getFirstElement();
						work.doSetChargerAssignmentRole((PrimaryObject) next,
								new CurrentAccountContext());
						super.doOK(is);
					} catch (Exception e) {
						MessageUtil.showToast(e.getMessage(), SWT.ICON_WARNING);
					}

				} else {
					if(work instanceof WorkDefinition){
						MessageUtil.showToast("请选择角色定义", SWT.ICON_WARNING);
					}else if(work instanceof Work){
						MessageUtil.showToast("请选择角色", SWT.ICON_WARNING);
					}
				}
			}
		};
		PrimaryObject master = work.getHoster();
		ns.setMaster(master);
		ns.show();
	}

	private String getNavigatorId(AbstractWork work) {
		return (work instanceof WorkDefinition)?"management.roledefinition":"project.role";
	}

}
