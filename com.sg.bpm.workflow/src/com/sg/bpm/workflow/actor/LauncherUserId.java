package com.sg.bpm.workflow.actor;

import com.sg.bpm.service.actor.IActorIdProvider;
import com.sg.widgets.part.CurrentAccountContext;

public class LauncherUserId implements IActorIdProvider {

	public LauncherUserId() {
	}

	@Override
	public String getActorId(Object[] input) {
		return new CurrentAccountContext().getAccountInfo().getUserId();
	}

}
