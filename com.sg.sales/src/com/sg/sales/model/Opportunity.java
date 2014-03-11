package com.sg.sales.model;

import com.mobnut.db.model.IContext;
import com.sg.sales.ISalesRole;
import com.sg.sales.ui.labelprovider.OpportunityCommonHTMLLable;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class Opportunity extends CompanyRelativeTeamControl implements
		IDataStatusControl {

	public static final String F_STATUS = "status";
	public static final String F_BUDGET = "budget";
	public static final String F_PROGRESS = "progress";

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
		if (canDelete(context)) {
			checkDataStatusForRemove(context);
			super.doRemove(context);
		} else {
			throw new Exception(MESSAGE_NOT_PERMISSION);
		}
	}

	@Override
	public void doUpdate(IContext context) throws Exception {
		if (canEdit(context)) {
			checkDataStatusForUpdate(context);
			super.doUpdate(context);
		} else {
			throw new Exception(MESSAGE_NOT_PERMISSION);
		}
	}

	@Override
	public boolean canDelete(IContext context) {
		try {
			if (isPersistent()) {
				checkDataStatusForRemove(context);
			}
		} catch (Exception e) {
			return false;
		}
		return super.canDelete(context);
	}

	@Override
	public boolean canEdit(IContext context) {
		try {
			if (isPersistent()) {
				checkDataStatusForUpdate(context);
			}
		} catch (Exception e) {
			return false;
		}
		return super.canEdit(context);
	}

	@Override
	public void checkDataStatusForUpdate(IContext context) throws Exception {
		if (!BASIC_VALUE_DEPOSITE.equals(getValue(F_STATUS))) {
			throw new Exception(MESSAGE_CANNOT_MODIFY);
		}
	}

	@Override
	public void checkDataStatusForRemove(IContext context) throws Exception {
		if (!BASIC_VALUE_EDITING.equals(getValue(F_STATUS))) {
			throw new Exception(MESSAGE_CANNOT_REMOVE);
		}
	}

	@Override
	public void checkDataStatusForApply(IContext context) throws Exception {
		if (!BASIC_VALUE_EDITING.equals(getValue(F_STATUS))) {
			throw new Exception(MESSAGE_CANNOT_APPLY);
		}
	}
	
	@Override
	protected String[] getRoleDesignatedUserFieldName() {
		return DESIGNATED_FIELDS_BY_ROLE;
	}
	
	@Override
	protected String getRoleNumberDesignatedUserField(String field) {
		if(F_SALES_SUP.equals(field)){
			return ISalesRole.SALES_SUPERVISOR_NUMBER;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter == CommonHTMLLabel.class){
			return (T)(new OpportunityCommonHTMLLable(this));
		}
		return super.getAdapter(adapter);
	}

	public Double getBudget() {
		return getDoubleValue(F_BUDGET);
	}

	public String getProgress() {
		return getStringValue(F_PROGRESS);
	}
}
