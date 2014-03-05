package com.sg.sales.model;

public class DataStatusControlUtil {

	public static String getStatusText(Object value) {
		if(IDataStatusControl.STATUS_VALUE_APPROVED.equals(value)){
			return IDataStatusControl.STATUS_TEXT_APPROVED;
		}
		if(IDataStatusControl.STATUS_VALUE_CHECKED.equals(value)){
			return IDataStatusControl.STATUS_TEXT_CHECKED;
		}
		if(IDataStatusControl.STATUS_VALUE_COMMITED.equals(value)){
			return IDataStatusControl.STATUS_TEXT_COMMITED;
		}
		if(IDataStatusControl.STATUS_VALUE_DEPOSITE.equals(value)){
			return IDataStatusControl.STATUS_TEXT_DEPOSITE;
		}
		if(IDataStatusControl.STATUS_VALUE_EDITING.equals(value)){
			return IDataStatusControl.STATUS_TEXT_EDITING;
		}
		return null;
	}

}
