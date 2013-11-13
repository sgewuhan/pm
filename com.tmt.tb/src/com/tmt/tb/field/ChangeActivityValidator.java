package com.tmt.tb.field;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;

public class ChangeActivityValidator extends AbstractValidator {

	public ChangeActivityValidator() {
	}
	@Override
	protected String getValidMessage(PrimaryObject data) {
		String choice = data.getStringValue("choice");
		if ("是".equals(choice)) {
			if (data instanceof TaskForm) {
				TaskForm taskForm = (TaskForm) data;
				Work work = taskForm.getWork();
				List<PrimaryObject> childrenWork = work.getChildrenWork();
				for (PrimaryObject po : childrenWork) {
					if (po instanceof ILifecycle) {
						ILifecycle iLifecycle = (ILifecycle) po;
						if (!ILifecycle.STATUS_FINIHED_VALUE.equals(iLifecycle
								.getLifecycleStatus())) {
							return "变更活动没有全部结束，不能完成变更";
						}

					}
				}

			}
		}
		return null;
	}

}
