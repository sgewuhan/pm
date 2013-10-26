package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;

public class RNDPeriodCost extends PrimaryObject implements IAccountPeriod {

	public static final String F_YEAR = "year";
	
	public static final String F_MONTH = "month";
	
	public static final String F_COSTCENTERCODE = "costcenter";

	@Override
	public Double getAccountValue(String accountNumber) {
		return (Double) getValue(accountNumber);
	}

}
