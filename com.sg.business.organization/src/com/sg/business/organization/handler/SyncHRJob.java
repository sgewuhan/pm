package com.sg.business.organization.handler;

import com.mobnut.commons.job.ScheduleRepeatJob;
import com.sg.business.organization.command.SyscHR;

public class SyncHRJob extends ScheduleRepeatJob {

	public SyncHRJob() {
		super("Sysc-HRHandler");
	}

	@Override
	protected void doJob() throws Exception {
		// ��HR����֯����ͬ��
		SyscHR syscHR = new SyscHR();
		syscHR.doSyscHROrganization();
	}

	public void stop() {
		removeJobChangeListener(this);
		cancel();
	}
}
