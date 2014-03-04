package com.sg.sales.model;

import org.bson.types.ObjectId;

public interface ICompanyRelative {

	public static final String F_COMPANY_ID = "company_id";

	ObjectId getCompanyId();
	
	Company getCompany();


}
