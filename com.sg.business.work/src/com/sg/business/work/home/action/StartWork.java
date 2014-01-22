package com.sg.business.work.home.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

import com.sg.business.commons.handler.LifeCycleActionStart;
import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;

public class StartWork extends AbstractWorkDetailPageAction {

	@Override
	protected void run(Work work, Control control) {
		LifeCycleActionStart start = new LifeCycleActionStart();
		CurrentAccountContext context = new CurrentAccountContext();
		try {
			int code = start.execute(work,context,Messages.get(control.getDisplay()).StartWork,work,null);
			if(SWT.YES == code){
				pageClear();
			}
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
		
	}
	
	@Override
	protected boolean visiableWhen(Work work) {
		return work.canStart();
	}

}
