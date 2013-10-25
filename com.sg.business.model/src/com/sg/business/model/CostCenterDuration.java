package com.sg.business.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.util.Util;

public class CostCenterDuration implements IAccountDuration{

	private Organization organization;

	private Map<String,Double> accountData = null;

	private Integer year;

	private Integer month;
	
	public CostCenterDuration(Organization organization) {
		this.organization = organization;
	}

	public CostCenterDuration() {
	}


	@Override
	public Double getAccountValue(String accountNumber) {
		loadData();
		return accountData.get(accountNumber);
	}

	private void loadData() {
		if(accountData==null){
			accountData = new HashMap<String,Double>();
			//¶ÁÈ¡Êý¾Ý
		}
	}

	@Override
	public void setYearDuration(int year) {
		this.year = year;
		accountData = null;
	}

	@Override
	public void setMonthDuration(int month) {
		this.month = month;
		accountData = null;
	}
	
	public void setOrganization(Organization org){
		if(Util.equals(organization, org)){
			return;
		}
		this.organization = org;
		accountData = null;
	}
	
	@Override
	public String toString() {
		if(organization == null){
			return "";
		}
		return organization.getCostCenterCode();
	}

}
