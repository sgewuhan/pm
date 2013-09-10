package com.sg.business.work;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.mobnut.commons.job.RepeatJob;

public class WorkSynchronizer extends RepeatJob {

	public WorkSynchronizer() {
		super("Work Synchronizer");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		//1. 取出用户
		
		
		return Status.OK_STATUS;
	}

}
