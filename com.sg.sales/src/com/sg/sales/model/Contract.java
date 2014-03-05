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
			addToOwnerList(signById);
		}
		
		super.doInsert(context);
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
	public String getStatusText() {
		return getStringValue(F_STATUS);
	}

	@Override
	public void checkDataStatusForRemove(IContext context) throws Exception {
		Object value = getValue(F_STATUS);
		if (!CONTRACT_VALUE_NOTVALID.equals(value)) {
			throw new Exception(MESSAGE_CANNOT_REMOVE);
		}	
	}

	@Override
	public void checkDataStatusForUpdate(IContext context) throws Exception {
		Object value = getValue(F_STATUS);
		if (!CONTRACT_VALUE_NOTVALID.equals(value)) {
			throw new Exception(MESSAGE_CANNOT_MODIFY);
		}			
	}

}
