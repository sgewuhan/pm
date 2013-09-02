package com.sg.business.project.test;

import org.eclipse.core.expressions.PropertyTester;

import com.sg.business.model.Project;

public class ProjectActionTest extends PropertyTester {

	private static final String PROPERTY_ACTION = "action";

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver instanceof Project) {
			Project project = (Project) receiver;
			if(property.equals(PROPERTY_ACTION)){
				if(args[0].equals("check")){
					
					return project.canCheck();
				}else if(args[0].equals("commit")){
					return project.canCommit();

				}else if(args[0].equals("start")){
					return project.canStart();

				}else if(args[0].equals("pause")){
					return project.canPause();

				}else if(args[0].equals("finish")){
					return project.canFinish();

				}else if(args[0].equals("cancel")){
					return project.canCancel();
				}
			}
		}
		return false;
	}

}
