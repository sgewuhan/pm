package com.sg.business.finance.rndcost;

import java.text.DecimalFormat;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.IAccountPeriod;

public class AccountDurationLabelProvider extends ColumnLabelProvider {

	private String accountNumber;

	public AccountDurationLabelProvider(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	@Override
	public String getText(Object element) {
		Double value = ((IAccountPeriod)element).getAccountValue(accountNumber);
		if(value == null){
			return "";
		}
		DecimalFormat df = new DecimalFormat("########.00");
		return df.format(value.doubleValue());
	}

	
}
