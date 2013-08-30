package com.sg.business.model.check;

import com.mobnut.db.model.PrimaryObject;

public interface ICheckListItem {

	public static final int WARRING = 1;
	public static final int	ERROR = 2;
	public static final int PASS = 0;
	
	String getMessage();
	
	int getType();

	String getTitle();

	PrimaryObject getSelection();

	String getEditorId();

	Object getData();
}
