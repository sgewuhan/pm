package com.sg.business.work.handler;

import org.eclipse.core.expressions.PropertyTester;

import com.sg.business.model.Work;

public class TaskActionTest extends PropertyTester {

	public TaskActionTest() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		Work work = (Work) receiver;
		if(!work.isExecuteWorkflowActivateAndAvailable()){
			return false;
		}
		
		return true;
	}

}
