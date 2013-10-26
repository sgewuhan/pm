package com.sg.business.finance.rndcost;

import com.sg.business.model.Organization;

public class CostCenterDurationQueryParameter {
	int year;
	int month;
	Organization organization;

	CostCenterDurationQueryParameter(int year, int month,
			Organization organization) {
		this.year = year;
		this.month = month;
		this.organization = organization;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + month;
		result = prime * result
				+ ((organization == null) ? 0 : organization.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CostCenterDurationQueryParameter other = (CostCenterDurationQueryParameter) obj;
		if (month != other.month)
			return false;
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		if (year != other.year)
			return false;
		return true;
	}




}