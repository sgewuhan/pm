package com.sg.business.document.nls;

import java.util.Locale;

import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages {
	private static final String BUNDLE_NAME = "com.sg.business.document.nls.messages"; //$NON-NLS-1$
	public String DocTypeOption_12;
	public String DocTypeOption_13;
	public String DocTypeOption_14;
	public String DocTypeOption_15;
	public String DocTypeOption_16;
	public String DocTypeOption_17;
	public String DocTypeOption_18;
	public String DocTypeOption_19;
	public String DocTypeOption_20;
	public String DocTypeOption_21;
	public String DocTypeOption_22;
	public String DocTypeOption_23;
	public String DocTypeOption_24;
	public String DocTypeOption_25;
	public String DocumentWorkflowHistory_0;
	public String DocumentWorkflowHistory_1;

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
