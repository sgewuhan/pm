package com.sg.business.taskform.javabean;

import java.util.HashMap;

public class ChangeWork {
	
	/**
	 * ������������_id
	 */
	private  String F_WORKD_ID;
	
	/**
	 * ����ĵ�_id
	 */
	private  String F_DOCUMENT_ID;
	
	/**
	 * �������������_id
	 */
	private String F_WORKCHARGER_ID;
	
	/**
	 * ����ִ������ִ����
	 */
	private HashMap F_WF_EXECUTE_ACTORS;
	
	public String getF_WORKD_ID() {
		return F_WORKD_ID;
	}

	public void setF_WORKD_ID(String f_WORKD_ID) {
		this.F_WORKD_ID = f_WORKD_ID;
	}

	public String getF_DOCUMENT_ID() {
		return F_DOCUMENT_ID;
	}

	public void setF_DOCUMENT_ID(String f_DOCUMENT_ID) {
		this.F_DOCUMENT_ID = f_DOCUMENT_ID;
	}

	public String getF_WORKCHARGER_ID() {
		return F_WORKCHARGER_ID;
	}

	public void setF_WORKCHARGER_ID(String f_WORKCHARGER_ID) {
		this.F_WORKCHARGER_ID = f_WORKCHARGER_ID;
	}

	public HashMap getF_WF_EXECUTE_ACTORS() {
		return F_WF_EXECUTE_ACTORS;
	}

	public void setF_WF_EXECUTE_ACTORS(HashMap f_WF_EXECUTE_ACTORS) {
		this.F_WF_EXECUTE_ACTORS = f_WF_EXECUTE_ACTORS;
	}



	public ChangeWork() {
		
	}

}
