package com.tmt.tb.validation;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IValidationHandler;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.Work;

public class ChangeActivityValidator implements IValidationHandler {

	public ChangeActivityValidator() {
	}

	@Override
	public boolean validate(PrimaryObject work) {
		if (work instanceof Work) {
			Work workchange = (Work) work;
			List<PrimaryObject> childrenWork = workchange.getChildrenWork();
			for (PrimaryObject po : childrenWork) {
				if (po instanceof ILifecycle) {
					ILifecycle iLifecycle = (ILifecycle) po;
					if (!ILifecycle.STATUS_FINIHED_VALUE.equals(iLifecycle
							.getLifecycleStatus())) {
						return false;
					}

				}
			}
		}
		return true;
	}

	@Override
	public String getMessage() {
		return "变更活动没有全部完成，不能完成此流程活动";
	}

}
