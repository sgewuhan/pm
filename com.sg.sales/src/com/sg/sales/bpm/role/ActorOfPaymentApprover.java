package com.sg.sales.bpm.role;

import com.sg.business.commons.actor.AbstractActorIdProvider;
import com.sg.business.model.Organization;
import com.sg.sales.ISalesRole;


public class ActorOfPaymentApprover extends AbstractActorIdProvider{

	@Override
	protected int getSelectType() {
		return Organization.ROLE_SEARCH_UP;
	}

	@Override
	protected String getRoleNumber() {
		return ISalesRole.ROLE_EXPENSE_APPROVER_NUMBER;
	}

}
