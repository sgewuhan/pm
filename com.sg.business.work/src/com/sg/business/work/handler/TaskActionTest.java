package com.sg.business.work.handler;

import org.eclipse.core.expressions.PropertyTester;

import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;

public class TaskActionTest extends PropertyTester {

	public static final String ARG0_SINGLE = "single";
	public static final String PROP_FINISH = "finish";
	public static final String PROP_START = "start";

	public TaskActionTest() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (!(receiver instanceof Work)) {
			return false;
		}
		Work work = (Work) receiver;
		String userId = new CurrentAccountContext().getConsignerId();
		if (!work.isExecuteWorkflowActivateAndAvailable()) {
			return false;
		}
		long count;
		if (PROP_START.equals(property)) { //$NON-NLS-1$
			count = work.countReservedUserTasks(userId);

		} else if (PROP_FINISH.equals(property)) { //$NON-NLS-1$
			count = work.countReservedAndInprogressUserTasks(userId);

		} else {
			return false;
		}
		if (count < 1) {
			return false;
		}
		Boolean value;
		if (args != null && args.length > 0 && ARG0_SINGLE.equals(args[0])) { //$NON-NLS-1$
			value = count == 1;
		} else {
			value = count > 1;
		}
		return value.equals(expectedValue);

	}

}
