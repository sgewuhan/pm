package com.tmt.tb.nls;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.tmt.tb.nls.messages"; //$NON-NLS-1$
	public String AbstractChangeActivityValidator_0;
	public String AddParticipateOfTB_10;
	public String AddParticipateOfTB_6;
	public String AddStandloneworkList_1;
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
	public String CreateWorkOrderToProjectOfTB_12;
	public String CreateWorkOrderToProjectOfTB_6;
	public String CreateWorkOrderToProjectOfTB_7;
	public String EngineeringChangePlan_1;
	public String EngineeringChangePlan_10;
	public String EngineeringChangePlan_12;
	public String EngineeringChangePlan_14;
	public String EngineeringChangePlan_16;
	public String EngineeringChangePlan_18;
	public String EngineeringChangePlan_2;
	public String EngineeringChangePlan_20;
	public String EngineeringChangePlan_22;
	public String EngineeringChangePlan_23;
	public String EngineeringChangePlan_24;
	public String EngineeringChangePlan_26;
	public String EngineeringChangePlan_4;
	public String EngineeringChangePlan_5;
	public String EngineeringChangePlan_6;
	public String EngineeringChangePlan_8;
	public String HasPlanValidation_4;
	public String LinkWorkToProjectOfTB_11;
	public String LinkWorkToProjectOfTB_5;
	public String LockBudgetToProjectOfTB_11;
	public String LockBudgetToProjectOfTB_13;
	public String LockBudgetToProjectOfTB_14;
	public String LockBudgetToProjectOfTB_5;
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

	public static Messages get() {
		return get(null);
	}

}