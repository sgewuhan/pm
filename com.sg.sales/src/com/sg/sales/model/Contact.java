package com.sg.sales.model;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;

public class Contact extends PrimaryObject implements ICompanyRelatied{
	
	
	public static final String F_LASTNAME = "lastname";
	public static final String F_MIDNAME = "midname";
	public static final String F_FIRSTNAME = "firstname";
	public static final String F_TEL1 = "tel_1";
	public static final String F_TEL2 = "tel_2";
	public static final String F_EMAIL = "email";

	@Override
	public boolean doSave(IContext context) throws Exception {
		String firstname = getStringValue(F_FIRSTNAME);
		String midname = getStringValue(F_MIDNAME);
		String lastname = getStringValue(F_LASTNAME);
		
		String desc = firstname+midname+lastname;
		
		setValue(F_DESC, desc);
		
		return super.doSave(context);
	}
	
	@Override
	public Object getCompanyId() {
		return getValue(F_COMPANY_ID);
	}
	
	
}
