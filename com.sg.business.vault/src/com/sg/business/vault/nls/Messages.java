package com.sg.business.vault.nls;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sg.business.vault.nls.messages"; //$NON-NLS-1$
	public String ContextSearchControl_0;
	public String ContextSearchControl_1;
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

	public static Messages get() {
		return get(null);
	}

}