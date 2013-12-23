package com.sg.business.performence.nls;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sg.business.performence.nls.messages"; //$NON-NLS-1$
	public String AddWorkRecord_0;
	public String AddWorkRecord_1;
	public String OrgProjectResCalender_0;
	public String OrgResCalender_0;
	public String ProjectResCalender_0;
	public String RuntimeWorkLabelprovider2_0;
	public String RuntimeWorkLabelprovider2_1;
	public String WorkListCellEditor_0;
	public String WorkListCellEditor_2;
	public String WorkListCellEditor_6;
	public String WorksFinishedPercentValidator_0;
	public String WorksFinishedPercentValidator_1;
	
	public static Messages get(Display display) {
		return NLS2.getMessage(BUNDLE_NAME, Messages.class, display);
	}

	public static Messages get() {
		return get(null);
	}

}