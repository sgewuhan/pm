package com.sg.business.pm2.nls;

import java.util.Locale;

import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages {
	private static final String BUNDLE_NAME = "com.sg.business.pm2.nls.messages"; //$NON-NLS-1$
	public String HeadArea_Logout_title;
	public String HeadArea_Logout_message;
	public String DBInit_0;
	public String DBInit_1;
	public String DBInit_10;
	public String DBInit_11;
	public String DBInit_2;
	public String DBInit_3;
	public String DBInit_4;
	public String DBInit_5;
	public String DBInit_6;
	public String DBInit_7;
	public String DBInit_8;
	public String DBInit_9;
	public String HeadArea_1;
	public String HeadArea_13;
	public String HeadArea_14;
	public String HeadArea_15;
	public String HeadArea_16;
	public String HeadArea_17;
	public String HeadArea_18;
	public String HeadArea_19;
	public String HeadArea_2;
	public String HeadArea_22;
	public String HeadArea_4;
	public String HeadArea_5;
	public String HeadArea_6;
	public String HeadArea_7;
	public String HeadArea_9;
	public String MntRole_0;
	public String MntRole_1;
	public String MntRole_2;
	
	public static Messages get(Display display) {
		return NLS2.getMessage(BUNDLE_NAME, Messages.class, display);
	}

	public static Messages get(Locale local) {
		return NLS2.getMessage(BUNDLE_NAME, Messages.class, local);
	}
	
	public static Messages get() {
		return NLS2.getMessage(BUNDLE_NAME, Messages.class);
	}

}