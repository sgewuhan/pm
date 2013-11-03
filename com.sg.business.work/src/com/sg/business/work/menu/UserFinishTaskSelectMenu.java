package com.sg.business.work.menu;

import java.util.List;

import com.sg.business.model.UserTask;
import com.sg.business.model.Work;

public class UserFinishTaskSelectMenu  extends AbstractUserTaskSelectMenu {

	@Override
	protected List<UserTask> getUserTask(Work work, String userId) {
		List<UserTask> result = work.getInprogressUserTasks(userId);
		result.addAll( work.getReservedUserTasks(userId));
		return result;
	}

	@Override
	protected String getCommandId() {
		return "runtimework.wffinish";
	}

	@Override
	protected String getContributionId() {
		return "work.processing.finishtask";
	}

}
