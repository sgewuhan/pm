package com.sg.sales.bpm.role;

import com.sg.sales.ISalesRole;


public class ActorOfPaymentFinancial extends ActorOfRole{

	@Override
	protected String getRoleId() {
		return ISalesRole.FINANCAIL_MANAGER_NUMBER;
	}

}
