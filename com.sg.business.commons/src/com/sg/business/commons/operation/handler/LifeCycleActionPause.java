package com.sg.business.commons.operation.handler;

import java.util.List;

import com.mobnut.db.model.IContext;
import com.sg.business.model.ILifecycle;

public class LifeCycleActionPause extends AbstractLifecycleAction {

	@Override
	protected List<Object[]> checkBeforeExecute(ILifecycle lc, IContext context)
			throws Exception {
		return lc.checkPauseAction(context);
	}

	@Override
	protected void execute(ILifecycle lc, IContext context) throws Exception {
		lc.doPause(context);
	}

}
