package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;

public interface ICheckListItem {

	public static final int WARRING = 1;
	public static final int	ERROR = 2;
	
	String getMessage();
	
	int getType();

	String getTitle();

	PrimaryObject getSelection();

	String getEditorId();
}
