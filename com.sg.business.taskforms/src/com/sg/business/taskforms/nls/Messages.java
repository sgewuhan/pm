package com.sg.business.taskforms.nls;

import java.util.Locale;

import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages {
	private static final String BUNDLE_NAME = "com.sg.business.taskforms.nls.messages"; //$NON-NLS-1$
	public String AbstractCheckFieldByChoice_1;
	public String DocumentApprovalMessageService_0;
	public String DocumentApprovalMessageService_1;
	public String DocumentApprovalMessageService_2;
	public String IRoleConstance_1;
	public String IRoleConstance_11;
	public String IRoleConstance_13;
	public String IRoleConstance_15;
	public String IRoleConstance_17;
	public String IRoleConstance_19;
	public String IRoleConstance_21;
	public String IRoleConstance_23;
	public String IRoleConstance_25;
	public String IRoleConstance_3;
	public String IRoleConstance_5;
	public String IRoleConstance_7;
	public String IRoleConstance_9;
	public String ProjectApproveMessageService_0;
	public String ProjectApproveMessageService_3;
	public String ProjectApproveMessageService_4;
	public String ProjectApproveMessageService_5;
	public String ProjectApproveMessageService_6;
	public String ProjectChangeMessageService_0;
	public String ProjectChangeMessageService_3;
	public String ProjectChangeMessageService_4;
	public String ProjectChangeMessageService_5;
	public String ProjectChangeMessageService_6;
	public String ProjectCloseMessageService_0;
	public String ProjectCloseMessageService_1;
	public String ProjectCloseMessageService_2;
	public String ReviewerMessageService_1;
	public String ReviewerMessageService_10;
	public String ReviewerMessageService_12;
	public String ReviewerMessageService_14;
	public String ReviewerMessageService_15;
	public String ReviewerMessageService_3;
	public String WorkSubconcessionsMessageService_0;
	public String WorkSubconcessionsMessageService_3;
	public String WorkSubconcessionsMessageService_4;
	public String WorkSubconcessionsMessageService_6;
	
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