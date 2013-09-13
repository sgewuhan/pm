package com.sg.business.commons.handler;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ILifecycle;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public abstract class AbstractLifecycleAction extends AbstractNavigatorHandler {

	public AbstractLifecycleAction() {
		super();
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if(selected instanceof ILifecycle){
			ViewerControl vc = getCurrentViewerControl(event);
			ILifecycle lc = (ILifecycle) selected;
			CurrentAccountContext context = new CurrentAccountContext();
			try {
				List<Object[]> message = checkBeforeExecute(lc,context);
				showCheckMessages(message);
				execute(lc,context);
				vc.getViewer().update(selected, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

	protected abstract List<Object[]> checkBeforeExecute(ILifecycle lc,IContext context) throws Exception;
	
	protected abstract void execute(ILifecycle lc,IContext context)  throws Exception;

	protected void showCheckMessages(List<Object[]> message) {
		
	}

}