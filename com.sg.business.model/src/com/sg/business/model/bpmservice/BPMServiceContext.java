package com.sg.business.model.bpmservice;

import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.IContext;

public class BPMServiceContext implements IContext {

	private String processId;
	private String processName;

	public BPMServiceContext(String processName,String processId){
		this.processName = processName;
		this.processId = processId;
	}
	
	@Override
	public AccountInfo getAccountInfo() {
		return new AccountInfo(processName, processId);
	}

	@Override
	public String getPartId() {
		return null;
	}

}
