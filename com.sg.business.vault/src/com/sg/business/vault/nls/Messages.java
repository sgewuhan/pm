package com.sg.business.vault.nls;

import java.util.Locale;

import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages {
	private static final String BUNDLE_NAME = "com.sg.business.vault.nls.messages"; //$NON-NLS-1$
	public String ContextSearchControl_0;
	public String ContextSearchControl_2;
	public String ContextSearchControl_3;
	public String CreateVaultDocument_0;
	public String DocumentCreater_0;
	public String FolderCreater_0;
	public String Index_0;
	public String Index_1;
	
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