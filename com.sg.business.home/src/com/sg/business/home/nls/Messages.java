package com.sg.business.home.nls;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sg.business.home.nls.messages"; //$NON-NLS-1$
	public String BulletinBoardView_0;

	public static Messages get(Display display) {
		return NLS2.getMessage(BUNDLE_NAME, Messages.class, display);
	}

	public static Messages get() {
		return get(null);
	}
}
