package com.sg.business.organization.handler;

import com.mobnut.commons.job.ScheduleRepeatJob;

public class SyscHRJob extends ScheduleRepeatJob {

	public SyscHRJob() {
		super("Sysc-HRHandler");
	}

	@Override
	protected void doJob() throws Exception {

		
	}

	public void stop() {
        removeJobChangeListener(this);
        cancel();
    }
}
