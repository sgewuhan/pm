package com.sg.business.organization.nls;

import org.eclipse.osgi.util.NLS;
import org.eclipse.rap.rwt.RWT;


public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sg.business.organization.nls.messages"; //$NON-NLS-1$
	public  String Consign_1;
	public  String Consign_2;
	public  String Consign_4;
	public  String OrgExchange_0;
	public  String OrgExchange_1;
	public  String OrgExchange_2;
	public  String OrgExchange_3;
	public  String RoleCreater_0;
	public  String SubTeamCreater_0;
	public  String SyncHR_0;
	public  String SyncHR_1;
	public  String SyncHR_2;
	public  String SyncHROrganizationDialog_0;
	public  String SyncHROrganizationDialog_1;
	public  String SyncHROrganizationDialog_2;
	public  String SyncHROrganizationDialog_3;
	public  String SyncHROrganizationDialog_4;
	public  String SyncHROrganizationDialog_5;
	public  String SyncHROrganizationDialog_6;
	public  String UserExchange_0;
	public  String UserExchange_1;
	public  String UserExchange_2;
	public  String UserExchange_3;
	public  String UserExchange_4;
	public  String UserExchange_5;
	public  String UserExchange_6;
	private Messages() {
	}

	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}
}
