package com.sg.sales.model;

import com.mobnut.db.model.IContext;

public class Opportunity extends CompanyRelativeTeamControl implements
		IDataStatusControl {
	
	public static final String F_STATUS = "status";

	@Override
	public String getStatusText() {
		Object value = getValue(F_STATUS);
		if (BASIC_VALUE_APPROVED.equals(value)) {
			return BASIC_TEXT_APPROVED;
		}
		if (BASIC_VALUE_CHECKED.equals(value)) {
			return BASIC_TEXT_CHECKED;
		}
		if (BASIC_VALUE_COMMITED.equals(value)) {
			return BASIC_TEXT_COMMITED;
		}
		if (BASIC_VALUE_DEPOSITE.equals(value)) {
			return BASIC_TEXT_DEPOSITE;
		}
		if (BASIC_VALUE_EDITING.equals(value)) {
			return BASIC_TEXT_EDITING;
		}
		return "";
	}

	@Override
	public void doRemove(IContext context) throws Exception {
		checkDataStatusForRemove(context);
		super.doRemove(context);
	}

	@Override
	public void doUpdate(IContext context) throws Exception {
		checkDataStatusForUpdate(context);
		super.doUpdate(context);
	}

	@Override
	public void checkDataStatusForUpdate(IContext context) throws Exception {
		Object value = getValue(F_STATUS);
		if (!BASIC_VALUE_DEPOSITE.equals(value)) {
			throw new Exception(MESSAGE_CANNOT_MODIFY);
		}		
	}

	@Override
	public void checkDataStatusForRemove(IContext context) throws Exception {
		Object value = getValue(F_STATUS);
		if (!BASIC_VALUE_EDITING.equals(value)) {
			throw new Exception(MESSAGE_CANNOT_REMOVE);
		}
	}
}
