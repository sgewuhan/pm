package com.tmt.kh.nls;

import java.util.Locale;

import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages {
	private static final String BUNDLE_NAME = "com.tmt.kh.nls.messages"; //$NON-NLS-1$
	public String ApproveMessageOfKH_1;
	public String ApproveMessageOfKH_11;
	public String ApproveMessageOfKH_13;
	public String ApproveMessageOfKH_15;
	public String ApproveMessageOfKH_18;
	public String ApproveMessageOfKH_20;
	public String ApproveMessageOfKH_22;
	public String ApproveMessageOfKH_25;
	public String ApproveMessageOfKH_27;
	public String ApproveMessageOfKH_32;
	public String ApproveMessageOfKH_4;
	public String ApproveMessageOfKH_41;
	public String ApproveMessageOfKH_43;
	public String ApproveMessageOfKH_45;
	public String ApproveMessageOfKH_47;
	public String ApproveMessageOfKH_6;
	public String ApproveMessageOfKH_8;

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