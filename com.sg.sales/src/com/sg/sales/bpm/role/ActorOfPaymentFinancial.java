package com.sg.sales.bpm.role;

import com.sg.business.commons.actor.AbstractActorIdProvider;
import com.sg.business.model.Organization;
import com.sg.sales.ISalesRole;

public class ActorOfPaymentFinancial extends AbstractActorIdProvider {

	@Override
	protected int getSelectType() {
		return Organization.ROLE_SEARCH_UP;
	}

	@Override
	protected String getRoleNumber() {
		return ISalesRole.FINANCAIL_MANAGER_NUMBER;
	}

}
