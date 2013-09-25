package com.sg.business.project.page;

import com.mobnut.db.model.PrimaryObject;

public class TransferPageConfig {
	private String sTitle = null;
	private String sDescription = null;
	private String navigatorid = null;
	private PrimaryObject master = null;
	public TransferPageConfig(String sTitle, String sDescription, String navigatorid,
			PrimaryObject master) {
		this.sTitle = sTitle;
		this.sDescription = sDescription;
		this.navigatorid= navigatorid;
		this.master = master;
	}
	public String getsTitle() {
		return sTitle;
	}
	public void setsTitle(String sTitle) {
		this.sTitle = sTitle;
	}
	public String getsDescription() {
		return sDescription;
	}
	public void setsDescription(String sDescription) {
		this.sDescription = sDescription;
	}
	public String getNavigatorid() {
		return navigatorid;
	}
	public void setNavigatorid(String navigatorid) {
		this.navigatorid = navigatorid;
	}
	public PrimaryObject getMaster() {
		return master;
	}
	public void setMaster(PrimaryObject master) {
		this.master = master;
	}
}
