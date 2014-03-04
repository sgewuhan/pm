package com.sg.sales.model;


public class Contract extends TeamControled  implements  ICompanyRelatied{

	@Override
	public Object getCompanyId() {
		return getValue(F_COMPANY_ID);
	}

}
