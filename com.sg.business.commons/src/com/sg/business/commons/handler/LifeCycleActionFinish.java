package com.sg.business.commons.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class LifeCycleActionFinish extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if(selected instanceof Project){
			ViewerControl vc = getCurrentViewerControl(event);
			Project project = (Project) selected;
			try {
				project.doFinish(new CurrentAccountContext());
				vc.getViewer().update(selected, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

}