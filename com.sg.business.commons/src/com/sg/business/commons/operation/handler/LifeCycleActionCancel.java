package com.sg.business.commons.operation.handler;

import java.util.List;

import org.eclipse.swt.SWT;

import com.mobnut.db.model.IContext;
import com.sg.business.model.ILifecycle;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;

public class LifeCycleActionCancel extends AbstractLifecycleAction {

	@Override
	protected List<Object[]> checkBeforeExecute(ILifecycle lc, IContext context)
			throws Exception {
		return lc.checkCancelAction(context);
	}

	@Override
	protected void execute(ILifecycle lc, IContext context) throws Exception {
		int yes = MessageUtil.showMessage(null, Messages.get().LifeCycleActionCancel_0,
				""+lc+Messages.get().LifeCycleActionCancel_2, //$NON-NLS-1$
				SWT.ICON_WARNING | SWT.YES | SWT.NO);
		if (yes == SWT.YES) {
			lc.doCancel(context);
		}
	}

}
