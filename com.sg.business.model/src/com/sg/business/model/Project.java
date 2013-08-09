package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;


public class Project extends PrimaryObject{

	
	private String projectNum;
	
	private Boolean activated;

	public String getProjectNum() {
		return projectNum;
	}

	public void setProjectNum(String projectNum) {
		this.projectNum = projectNum;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}
	
	
	
}
