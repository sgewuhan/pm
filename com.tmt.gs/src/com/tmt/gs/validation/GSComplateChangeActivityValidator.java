package com.tmt.gs.validation;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IValidationHandler;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.Work;
import com.tmt.gs.nls.Messages;

public class GSComplateChangeActivityValidator implements IValidationHandler {

	public GSComplateChangeActivityValidator() {
	}

	@Override
	public boolean validate(PrimaryObject work) {
		if (work instanceof Work) {
			Work workchange = (Work) work;
			List<PrimaryObject> ecnList = workchange.getChildrenWork();
			for (PrimaryObject ecn : ecnList) {
				ILifecycle iLifecycle = (ILifecycle) ecn;
				if (!(ILifecycle.STATUS_FINIHED_VALUE.equals(iLifecycle
						.getLifecycleStatus()) || ILifecycle.STATUS_CANCELED_VALUE
						.equals(iLifecycle.getLifecycleStatus()))) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String getMessage() {
		return Messages.get().ComplateChangeActivityValidator_0;
	}

}
