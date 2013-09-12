package com.sg.business.model.event;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.IAccountEvent;

public class AccountEvent implements IAccountEvent {

	private String code;
	private PrimaryObject data;

	public AccountEvent(String code,PrimaryObject data){
		this.code = code;
		this.data = data;
	}
	
	@Override
	public String getEventCode() {
		return code;
	}

	@Override
	public Object getEventData() {
		return data;
	}

}
