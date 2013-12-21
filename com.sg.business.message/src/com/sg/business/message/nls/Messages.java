package com.sg.business.message.nls;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sg.business.message.nls.messages"; //$NON-NLS-1$
	public String AddReciever_1;
	public String AddReciever_2;
	public String ReadMark_0;
	public String SendToSelectedUser_0;

	public static Messages get(Display display) {
		return	NLS2.getMessage(BUNDLE_NAME, Messages.class,display);
	}
	
	public static Messages get(){
		return get(null);
	}

}
