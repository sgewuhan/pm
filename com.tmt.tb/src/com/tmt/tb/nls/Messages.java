package com.tmt.tb.nls;

import java.util.Locale;

import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages {
	private static final String BUNDLE_NAME = "com.tmt.tb.nls.messages"; //$NON-NLS-1$
	public String AbstractChangeActivityValidator_0;
	public String AddAllUserList_1;
	public String ApplicationMessageOfTB_1;
	public String ApplicationMessageOfTB_10;
	public String ApplicationMessageOfTB_11;
	public String ApplicationMessageOfTB_2;
	public String ApplicationMessageOfTB_23;
	public String ApplicationMessageOfTB_25;
	public String ApplicationMessageOfTB_27;
	public String ApplicationMessageOfTB_4;
	public String ApplicationMessageOfTB_5;
	public String ApplicationMessageOfTB_8;
	public String ComplateChangeActivityValidator_0;
	public String HasPlanValidation_4;
	public String LinkWorkToProjectOfTB_11;
	public String LinkWorkToProjectOfTB_5;
	public String ProjectBudgetOfCreateProjectPage_1;
	public String ProjectBudgetOfCreateProjectPage_2;
	public String ProjectBudgetOfTaskFormPage_0;
	public String ProjectBudgetOfTaskFormPage_1;
	public String ProjectChangeConfirmProgramSaveHandler_12;
	public String ProjectChangeConfirmProgramSaveHandler_4;
	public String ProjectChangeConfirmProgramSaveHandler_5;
	public String ProjectChangeConfirmProgramSaveHandler_6;
	public String RelatedDeptValidation_4;
	public String StandloneWorkPageOfTB_0;
	public String StandloneWorkPageOfTB_4;
	public String StandloneWorkPageOfTB_5;

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