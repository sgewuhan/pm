package com.sg.business.finance.nls;

import java.util.Locale;

import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages {
	private static final String BUNDLE_NAME = "com.sg.business.finance.nls.messages"; //$NON-NLS-1$
	public String BudgetItemEditor_0;
	public String BudgetItemEditor_1;
	public String BudgetItemEditor_10;
	public String BudgetItemEditor_11;
	public String BudgetItemEditor_12;
	public String BudgetItemEditor_13;
	public String BudgetItemEditor_14;
	public String BudgetItemEditor_15;
	public String BudgetItemEditor_2;
	public String BudgetItemEditor_3;
	public String BudgetItemEditor_4;
	public String BudgetItemEditor_5;
	public String BudgetItemEditor_6;
	public String BudgetItemEditor_8;
	public String BudgetItemEditor_9;
	public String BudgetItemFunction_1;
	public String CostCenterViewer_0;
	public String CostCenterViewer_1;
	public String CostCenterViewer_2;
	public String RNDCostAdjustmentView_0;
	public String WorkOrderCostViewer_3;


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
