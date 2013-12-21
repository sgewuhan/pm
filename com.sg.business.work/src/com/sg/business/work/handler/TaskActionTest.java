package com.sg.business.work.handler;

import org.eclipse.core.expressions.PropertyTester;

import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;

public class TaskActionTest extends PropertyTester {

	public TaskActionTest() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if(!(receiver instanceof Work)){
			return false;
		}
		Work work = (Work) receiver;
		String userId = new CurrentAccountContext().getConsignerId();
		if(!work.isExecuteWorkflowActivateAndAvailable()){
			return false;
		}
		long count;
		if("start".equals(property)){ //$NON-NLS-1$
			count = work.countReservedUserTasks(userId);
			
		}else if("finish".equals(property)){ //$NON-NLS-1$
			count=work.countReservedAndInprogressUserTasks(userId);
			
		}else{
			return false;
		}
		if(count<1){
			return false;
		}
		Boolean value;
		if("single".equals(args[0])){ //$NON-NLS-1$
			value=count==1;
		}else{
			value=count>1;
		}
		return value.equals(expectedValue);
		
	}

}
