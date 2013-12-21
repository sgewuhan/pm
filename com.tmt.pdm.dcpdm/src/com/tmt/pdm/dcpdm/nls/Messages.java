package com.tmt.pdm.dcpdm.nls;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.tmt.pdm.dcpdm.nls.messages"; //$NON-NLS-1$
	public String DCPDMUtil_2;
	public String DCPDMUtil_3;
	public String LinkPDMDocAndDraw2_0;
	public String LinkPDMDocAndDraw2_1;
	public String PDMObjectSelector_0;
	public String PDMObjectSelector_1;
	public String PDMObjectSelector_11;
	public String PDMObjectSelector_4;
	public String PDMObjectSelector_5;

	public static Messages get(Display display) {
		return NLS2.getMessage(BUNDLE_NAME, Messages.class, display);
	}

	public static Messages get() {
		return get(null);
	}

}