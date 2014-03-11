package com.sg.sales.bpm.role;

import com.sg.sales.ISalesRole;


public class ActorOfPaymentApprover extends ActorOfRole{

	@Override
	protected String getRoleId() {
		return ISalesRole.ROLE_EXPENSE_APPROVER_NUMBER;
	}

}
