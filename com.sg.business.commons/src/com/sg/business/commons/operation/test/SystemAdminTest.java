package com.sg.business.commons.operation.test;

import org.eclipse.core.expressions.PropertyTester;

import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.IContext;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class SystemAdminTest extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		IContext context = new CurrentAccountContext();
		if ("synchr".equals(args[0])) { //$NON-NLS-1$
			if (context != null) {
				AccountInfo accountInfo = context.getAccountInfo();
				if (accountInfo != null) {
					String userId = accountInfo.getUserId();
					User user = UserToolkit.getUserById(userId);
					return user.isAdmin();
				}
			}
		}
		return false;
	}

}
