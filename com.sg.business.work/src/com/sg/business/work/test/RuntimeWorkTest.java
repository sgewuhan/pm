package com.sg.business.work.test;

import org.eclipse.core.expressions.PropertyTester;

import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;

public class RuntimeWorkTest extends PropertyTester {

	private static final String PROPERTY_ACTION = "action";

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver instanceof Work) {
			Work work = (Work) receiver;
			if (property.equals(PROPERTY_ACTION)) {
				if ("createChildWork".equals(args[0])) {
					return work.canCreateChildWork(new CurrentAccountContext());
				} else if ("createDeliverable".equals(args[0])) {
					return work
							.canCreateDeliverable(new CurrentAccountContext());
				} else if ("reassignment".equals(args[0])) {
					return work.canReassignment(new CurrentAccountContext());
				} else if ("modify".equals(args[0])) {
					return work.canEdit(new CurrentAccountContext());
				} else if ("workrecord".equals(args[0])) {
					return work.canEditWorkRecord(new CurrentAccountContext());
				} else if ("openproject".equals(args[0])) {
					return work.getProjectId() != null;
				}else if ("delete".equals(args[0])) {
					return work.canDelete(new CurrentAccountContext());
				}
			}

		}
		return true;
	}
}
