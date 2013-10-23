package com.sg.bpm.workflow.actor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.service.actor.IActorIdProvider;

public class LauncherUserId implements IActorIdProvider {

	public LauncherUserId() {
	}

	@Override
	public String getActorId(Object[] input) {
		PrimaryObject work = (PrimaryObject) input[0];
		String value = (String) work.getValue("chargerid", true);
		return value;
	}

}
