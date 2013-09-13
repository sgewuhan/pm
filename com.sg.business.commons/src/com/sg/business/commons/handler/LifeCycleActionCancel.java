package com.sg.business.commons.handler;

import java.util.List;

import com.mobnut.db.model.IContext;
import com.sg.business.model.ILifecycle;

public class LifeCycleActionCancel extends AbstractLifecycleAction {

	@Override
	protected List<Object[]> checkBeforeExecute(ILifecycle lc, IContext context)
			throws Exception {
		return lc.checkCancelAction(context);
	}

	@Override
	protected void execute(ILifecycle lc, IContext context) throws Exception {
		lc.doCancel(context);
	}

}
