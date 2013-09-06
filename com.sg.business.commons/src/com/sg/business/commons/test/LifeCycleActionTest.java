package com.sg.business.commons.test;

import org.eclipse.core.expressions.PropertyTester;

import com.sg.business.model.ILifecycle;

public class LifeCycleActionTest extends PropertyTester {

	private static final String PROPERTY_ACTION = "action";

	@Override
	public boolean test(Object receiver, String properties, Object[] args,
			Object expectedValue) {
		if (receiver instanceof ILifecycle) {
			ILifecycle lc = (ILifecycle) receiver;
			if (properties.equals(PROPERTY_ACTION)) {
				if (args[0].equals("check")) {
					return lc.canCheck();
					
				} else if (args[0].equals("commit")) {
					return lc.canCommit();

				} else if (args[0].equals("start")) {
					return lc.canStart();

				} else if (args[0].equals("pause")) {
					return lc.canPause();

				} else if (args[0].equals("finish")) {
					return lc.canFinish();

				} else if (args[0].equals("cancel")) {
					return lc.canCancel();
				}
			}
		}
		return false;
	}

}
