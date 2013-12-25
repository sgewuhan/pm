package com.tmt.jszx.nls;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.tmt.jszx.nls.messages"; //$NON-NLS-1$
	public String ProjectApplyArchiveService_0;
	public String ProjectApplyMessageService_0;
	public String ProjectApplyMessageService_3;
	public String ProjectApplyMessageService_4;
	public String ProjectApplyMessageService_5;
	public String ProjectApplyMessageService_6;
	public String ProjectReviewServiceOfJSZX_0;
	public String ProjectReviewServiceOfJSZX_1;
	public String ProjectReviewServiceOfJSZX_10;
	public String ProjectReviewServiceOfJSZX_12;
	public String ProjectReviewServiceOfJSZX_14;
	public String ProjectReviewServiceOfJSZX_19;
	public String ProjectReviewServiceOfJSZX_21;
	public String ProjectReviewServiceOfJSZX_3;
	public String ProjectReviewServiceOfJSZX_5;
	public String ProjectReviewServiceOfJSZX_7;
	public String ProjectReviewServiceOfJSZX_9;

	public static Messages get(Display display) {
		return NLS2.getMessage(BUNDLE_NAME, Messages.class, display);
	}

	public static Messages get() {
		return get(null);
	}

}