package com.sg.business.model;

import com.mobnut.db.model.IContext;

public interface IActivateSwitch {

	/**
	 *  «∑Ò∆Ù”√
	 */
	public static final String F_ACTIVATED = "activated";
	
	void doSwitchActivation(IContext context)throws Exception;

	boolean isActivated();

	void setActivated(Boolean activated);

}
