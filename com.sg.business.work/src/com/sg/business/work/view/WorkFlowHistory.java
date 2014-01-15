package com.sg.business.work.view;

import org.drools.runtime.process.ProcessInstance;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPart;

import com.sg.business.commons.ui.flow.ProcessHistoryUIToolkit;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.widgets.part.view.TableNavigator;

public class WorkFlowHistory extends TableNavigator {

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		Table table = (Table) navi.getControl();
		ProcessHistoryUIToolkit.handleProcessHistoryTable(table);
	}

	@Override
	protected void updatePartName(IWorkbenchPart part) {
		if (master instanceof Work) {
			String workname = master.getLabel();
			ProcessInstance executeProcess = ((Work) master)
					.getExecuteProcess();
			if (executeProcess == null) {
				setPartName(workname); //$NON-NLS-1$
			} else {
				String name = executeProcess.getProcess().getName();
				setPartName(workname + "|" + name); //$NON-NLS-1$
			}
		} else if (master instanceof UserTask) {
			String workname = ((UserTask) master).getWorkName();
			String name = ((UserTask) master).getProcessName();
			setPartName(workname + "|" + name); //$NON-NLS-1$
		} else {
			setPartName(originalPartName);
		}

	}
}
