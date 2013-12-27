package com.tmt.kfzx.nls;

import java.util.Locale;

import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages {
	private static final String BUNDLE_NAME = "com.tmt.kfzx.nls.messages"; //$NON-NLS-1$
	public String ApproveMessageOfKFZX_1;
	public String ApproveMessageOfKFZX_11;
	public String ApproveMessageOfKFZX_4;
	public String ApproveMessageOfKFZX_6;
	public String MeetingMessageOfKFZX_0;
	public String MeetingMessageOfKFZX_1;
	public String MeetingMessageOfKFZX_10;
	public String MeetingMessageOfKFZX_12;
	public String MeetingMessageOfKFZX_13;
	public String MeetingMessageOfKFZX_15;
	public String MeetingMessageOfKFZX_2;
	public String MeetingMessageOfKFZX_3;
	public String MeetingMessageOfKFZX_5;
	public String MeetingMessageOfKFZX_8;
	public String projectstartMessageOfKFZX_0;
	public String projectstartMessageOfKFZX_1;
	public String projectstartMessageOfKFZX_10;
	public String projectstartMessageOfKFZX_12;
	public String projectstartMessageOfKFZX_4;
	public String projectstartMessageOfKFZX_7;
	public String projectstartMessageOfKFZX_9;

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