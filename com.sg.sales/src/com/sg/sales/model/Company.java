package com.sg.sales.model;


public class Company extends TeamControled implements IDataStatusControl {

	@Override
	public String getStatusText() {
		Object value = getValue(IDataStatusControl.F_STATUS);
		return DataStatusControlUtil.getStatusText(value);

	}


}
