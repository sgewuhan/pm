package com.tmt.gs.nls;

import java.util.Locale;

import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages {
	private static final String BUNDLE_NAME = "com.tmt.gs.nls.messages"; //$NON-NLS-1$
	public String AbstractChangeActivityValidator_0;
	public String AddAllUserList_1;
	public String GSApplicationMessageOfTB_1;
	public String GSApplicationMessageOfTB_10;
	public String GSApplicationMessageOfTB_11;
	public String GSApplicationMessageOfTB_2;
	public String GSApplicationMessageOfTB_23;
	public String GSApplicationMessageOfTB_25;
	public String GSApplicationMessageOfTB_27;
	public String GSApplicationMessageOfTB_4;
	public String GSApplicationMessageOfTB_5;
	public String GSApplicationMessageOfTB_8;
	public String ComplateChangeActivityValidator_0;
	public String EngineeringChangePlan_1;
	public String EngineeringChangePlan_10;
	public String EngineeringChangePlan_12;
	public String EngineeringChangePlan_14;
	public String EngineeringChangePlan_16;
	public String EngineeringChangePlan_18;
	public String EngineeringChangePlan_20;
	public String EngineeringChangePlan_22;
	public String EngineeringChangePlan_23;
	public String EngineeringChangePlan_24;
	public String EngineeringChangePlan_26;
	public String EngineeringChangePlan_4;
	public String EngineeringChangePlan_5;
	public String EngineeringChangePlan_6;
	public String EngineeringChangePlan_8;
	public String HasCustomerValidation_4;
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