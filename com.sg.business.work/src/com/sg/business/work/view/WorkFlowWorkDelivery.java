package com.sg.business.work.view;

import org.eclipse.ui.IWorkbenchPart;

import com.sg.business.model.UserTask;
import com.sg.widgets.part.view.TableNavigator;

public class WorkFlowWorkDelivery extends TableNavigator {

	@Override
	protected void updatePartName(IWorkbenchPart part) {
		if (master != null) {
			String workname = ((UserTask)master).getWorkName();
			setPartName(workname+" ÎÄµµ"); 
		} else {
			setPartName(originalPartName);
		}
	}

}
