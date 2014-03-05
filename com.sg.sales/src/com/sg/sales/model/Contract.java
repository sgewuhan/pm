package com.sg.sales.model;

import com.mobnut.db.model.IContext;

public class Contract extends CompanyRelativeTeamControl {

	public static final String F_SIGNBY = "signby";

	@Override
	public void doInsert(IContext context) throws Exception {
		//合同的签订人需要自动加为所有者
		String signById = (String) getValue(F_SIGNBY);
		if(signById!=null){
			addToOwnerList(signById);
		}
		
		super.doInsert(context);
	}

}
