package com.sg.business.commons.operation.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

import com.sg.business.commons.operation.handler.LifeCycleActionFinish;
import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;

public class FinishWork extends AbstractWorkDetailPageAction {

	@Override
	public void run(Work work, Control control) {
		LifeCycleActionFinish finish = new LifeCycleActionFinish();
		CurrentAccountContext context = new CurrentAccountContext();
		try {
			int code = finish.execute(work,context,Messages.get(control.getDisplay()).FinishWork,work,null);
			if(SWT.YES == code){
				pageClear();
			}
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
		
	}

	@Override
	protected boolean visiableWhen(Work work) {
		return work.canFinish();
	}

}
