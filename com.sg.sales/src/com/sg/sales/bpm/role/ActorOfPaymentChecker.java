package com.sg.sales.bpm.role;

import com.sg.sales.ISalesRole;


public class ActorOfPaymentChecker extends ActorOfRole{

	@Override
	protected String getRoleId() {
		return ISalesRole.ROLE_EXPENSE_CHECK_NUMBER;
	}

}
