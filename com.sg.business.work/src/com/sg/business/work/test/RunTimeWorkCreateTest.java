package com.sg.business.work.test;

import org.eclipse.core.expressions.PropertyTester;

import com.sg.business.model.Work;

public class RunTimeWorkCreateTest extends PropertyTester {

	private static final String PROPERTY_ACTION = "action";

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver instanceof Work) {
			Work work = (Work) receiver;
			if (property.equals(PROPERTY_ACTION)) {
				if (args[0].equals("runTimeWorkCreate")) {
					return work.canRunTimeCreate();
				} else if(args[0].equals("runTimeWorkCreateDeliverable")) {
					return work.canRunTimeCreateDeliverable();
				}
			}

		}
		return true;
	}

}
