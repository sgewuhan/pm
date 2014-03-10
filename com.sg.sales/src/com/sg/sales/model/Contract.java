package com.sg.sales.model;

import com.mobnut.db.model.IContext;

public class Contract extends CompanyRelativeTeamControl implements IDataStatusControl{

	public static final String F_SIGNBY = "signby";
	
	public static final String F_STATUS = "contractstatus";

	@Override
	public void doInsert(IContext context) throws Exception {
		//合同的签订人需要自动加为所有者
		String signById = (String) getValue(F_SIGNBY);
		if(signById!=null){
			addToPermissionOwnerList(signById);
		}
		super.doInsert(context);
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
	public String getStatusText() {
		return getStringValue(F_STATUS);
	}

	@Override
	public void checkDataStatusForRemove(IContext context) throws Exception {
		if (!CONTRACT_VALUE_NOTVALID.equals(getValue(F_STATUS))) {
			throw new Exception(MESSAGE_CANNOT_REMOVE);
		}	
	}

	@Override
	public void checkDataStatusForUpdate(IContext context) throws Exception {
		if (!CONTRACT_VALUE_NOTVALID.equals(getValue(F_STATUS))) {
			throw new Exception(MESSAGE_CANNOT_MODIFY);
		}			
	}

	@Override
	public void checkDataStatusForApply(IContext context) throws Exception {
		if(!CONTRACT_VALUE_NOTVALID.equals(getValue(F_STATUS))){
			throw new Exception(MESSAGE_CANNOT_APPLY);
		}		
	}
}
