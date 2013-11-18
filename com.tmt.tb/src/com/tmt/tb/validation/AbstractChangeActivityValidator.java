package com.tmt.tb.validation;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IValidationHandler;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.Work;

public abstract class AbstractChangeActivityValidator implements
		IValidationHandler {

	public AbstractChangeActivityValidator() {
	}

	@Override
	public boolean validate(PrimaryObject work) {
		String ecnName = getECNName();
		if (ecnName == null) {
			return true;
		}
		if (work instanceof Work) {
			Work workchange = (Work) work;
			List<PrimaryObject> ecnList = workchange.getChildrenWork();
			for (PrimaryObject ecn : ecnList) {
				if (ecnName.equals(ecn.getDesc())) {
					ILifecycle iLifecycle = (ILifecycle) ecn;
					if (!(ILifecycle.STATUS_FINIHED_VALUE.equals(iLifecycle
							.getLifecycleStatus()) || ILifecycle.STATUS_CANCELED_VALUE
							.equals(iLifecycle.getLifecycleStatus()))) {
						return false;
					}

				}
			}
		}
		return true;
	}

	public abstract String getECNName();

	@Override
	public String getMessage() {
		return "变更活动没有全部完成，不能完成此流程活动";
	}

}
