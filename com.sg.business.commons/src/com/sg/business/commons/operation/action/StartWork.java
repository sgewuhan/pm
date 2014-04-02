package com.sg.business.commons.operation.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

import com.sg.business.commons.operation.handler.LifeCycleActionStart;
import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.model.Work;
import com.sg.business.model.WorkflowSynchronizer;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.view.SideBarNavigator;

public class StartWork extends AbstractWorkDetailPageAction {

	@Override
	public void run(Work work, Control control) {
		LifeCycleActionStart start = new LifeCycleActionStart();
		CurrentAccountContext context = new CurrentAccountContext();
		try {
			int code = start.execute(work, context,
					Messages.get(control.getDisplay()).StartWork, work, null);
			if (SWT.YES == code) {
				WorkflowSynchronizer synchronizer = new WorkflowSynchronizer();
				synchronizer
						.synchronizeUserTask(context.getConsignerId(), work);

				refreshSideBar("1", work);
				pageReload(true);
			}
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

	}

	private void refreshSideBar(String id, Work work) {
		SideBarNavigator sidebar = UIFrameworkUtils.getSidebar();
		sidebar.doRefresh(id, work);
	}

	@Override
	protected boolean visiableWhen(Work work) {
		return work.canStart();
	}

}
