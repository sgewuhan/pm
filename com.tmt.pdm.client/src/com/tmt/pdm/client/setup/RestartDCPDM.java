package com.tmt.pdm.client.setup;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.tmt.pdm.client.Starter;

public class RestartDCPDM implements ISchedualJobRunnable {

	@Override
	public boolean run() throws Exception {
		Starter.getStarter().lazyLoadService();
		return true;
	}

}
