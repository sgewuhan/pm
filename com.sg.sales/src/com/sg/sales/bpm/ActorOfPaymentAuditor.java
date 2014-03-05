package com.sg.sales.bpm;

import com.sg.sales.ISalesRole;


public class ActorOfPaymentAuditor extends ActorOfRole{

	@Override
	protected String getRoleId() {
		return ISalesRole.TEAM_MANAGER_NUMBER;
	}

}
