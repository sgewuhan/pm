package com.sg.business.model;

public interface IAccountDuration {
	
	void setYearDuration(int year);
	
	void setMonthDuration(int month);

	Double getAccountValue(String accountNumber);

}
