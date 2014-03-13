package com.sg.sales.model;

import com.mobnut.db.model.IContext;
import com.sg.sales.ISalesRole;
import com.sg.sales.model.input.CompanyEditorInputFactory;
import com.sg.widgets.commons.model.IEditorInputFactory;

public class Company extends TeamControl implements IDataStatusControl, ISalesTeam {

	public static final String F_STATUS = "status";
	public static final String F_LEVEL = "level";
	public static final String F_SERVICELEVEL = "levelservice";

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
		Object value = getValue(F_STATUS);
		if (!BASIC_VALUE_DEPOSITE.equals(value)&&!BASIC_VALUE_EDITING.equals(value)) {
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

	@Override
	public void checkDataStatusForApply(IContext context) throws Exception {
		if (!BASIC_VALUE_EDITING.equals(getValue(F_STATUS))) {
			throw new Exception(MESSAGE_CANNOT_APPLY);
		}
	}

	@Override
	protected String getPermissionRoleNumber() {
		return ISalesRole.CRM_ADMIN_NUMBER;
	}

	@Override
	protected String[] getVisitableFields() {
		return VISIABLE_FIELDS;
	}

	@Override
	protected String[] getDuplicateTeamFields() {
		return VISIABLE_FIELDS;
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

	public String getLevel() {
		return getStringValue(F_LEVEL);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter == IEditorInputFactory.class) {
			return (T) (new CompanyEditorInputFactory(this));
		}
		return super.getAdapter(adapter);
	}

}
