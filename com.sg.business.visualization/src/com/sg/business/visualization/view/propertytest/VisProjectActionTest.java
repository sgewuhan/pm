package com.sg.business.visualization.view.propertytest;

import org.eclipse.core.expressions.PropertyTester;

import com.sg.business.model.Project;

public class VisProjectActionTest extends PropertyTester {

	public VisProjectActionTest() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if(receiver instanceof Project){
			return true;
		}else{
			return false;
		}
	}

}
