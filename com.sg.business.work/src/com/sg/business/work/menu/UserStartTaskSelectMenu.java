package com.sg.business.work.menu;

import java.util.List;

import com.sg.business.model.UserTask;
import com.sg.business.model.Work;


public class UserStartTaskSelectMenu extends AbstractUserTaskSelectMenu {

	@Override
	protected List<UserTask> getUserTask(Work work, String userId) {
		return work.getReservedUserTasks(userId);
	}

	@Override
	protected String getContributionId() {
		return "work.processing.starttask"; //$NON-NLS-1$
	}

	@Override
	protected String getCommandId() {
		return "runtimework.wfstart"; //$NON-NLS-1$
	}

	
}
