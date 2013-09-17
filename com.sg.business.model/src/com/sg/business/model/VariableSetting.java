package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;

public class VariableSetting extends PrimaryObject {
	
	public static final String F_VARID = "varid";
	
	public static final String F_PERSONAL = "personal";

	public  String getfVarid() {
		return (String) getValue(F_VARID);
	}

	public  String getfPersonal() {
		return (String) getValue(F_PERSONAL);
	}
	
	
	

}
