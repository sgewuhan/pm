package com.sg.business.work.nls;

import java.util.Locale;

import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.NLS2;

public class Messages {
	private static final String BUNDLE_NAME = "com.sg.business.work.nls.messages"; //$NON-NLS-1$
	public String AddWorkRecord_0;
	public String AddWorkRecord_1;
	public String FinishTask;
	public String StartWork;
	public String FinishWork;
	public String AssignWork;
	public String AddParticipateFromOrg_0;
	public String AddParticipateFromOrg_1;
	public String AddRuntimeWorkParticipate_1;
	public String AddRuntimeWorkParticipate_2;
	public String CompleteTask_2;
	public String CompleteTask_3;
	public String CompleteTask_4;
	public String CompleteTask_6;
	public String ConfirmPage_1;
	public String ConfirmPage_10;
	public String ConfirmPage_19;
	public String ConfirmPage_2;
	public String ConfirmPage_29;
	public String ConfirmPage_3;
	public String ConfirmPage_30;
	public String ConfirmPage_31;
	public String ConfirmPage_4;
	public String ConfirmPage_5;
	public String ConfirmPage_6;
	public String ConfirmPage_7;
	public String ConfirmPage_8;
	public String ConfirmPage_9;
	public String CreateDeliverable_0;
	public String CreateDeliverable_1;
	public String CreateDeliverable_2;
	public String CreateDeliverable_3;
	public String CreateDeliverable_4;
	public String CreateDeliverable_5;
	public String LaunchWorkWizard_1;
	public String LinkDeliverable_0;
	public String LinkDeliverable_1;
	public String LinkDeliverable_2;
	public String LinkDeliverable_3;
	public String LinkDeliverable_5;
	public String LinkDeliverable_6;
	public String LinkDeliverable_7;
	public String LinkDeliverable_8;
	public String SelectWorkDefinitionPage_1;
	public String SendMessage_0;
	public String SendMessage_1;
	public String SendMessage_3;
	public String SendMessage_5;
	public String StartTask_2;
	public String StartTask_3;
	public String StartTask_4;
	public String UserTaskDocumentLabelProvider_0;
	public String UserTaskDocumentLabelProvider_1;
	public String UserTaskDocumentLabelProvider_2;
	public String UserTaskDocumentLabelProvider_3;
	public String UserTaskHistoryLabelProvider_0;
	public String UserTaskHistoryLabelProvider_1;
	public String UserTaskHistoryLabelProvider_2;
	public String UserTaskHistoryLabelProvider_3;
	public String UserTaskHistoryLabelProvider_4;
	public String UserTaskHistoryLabelProvider_5;
	public String UserTaskHistoryLabelProvider_6;
	public String UserTaskLabelProvider_0;
	public String WorkFilterAction_10;
	public String WorkFilterAction_11;
	public String WorkFilterAction_12;
	public String WorkFilterAction_13;
	public String WorkFilterAction_14;
	public String WorkFilterAction_15;
	public String WorkFilterAction_16;
	public String WorkFilterAction_5;
	public String WorkFilterAction_6;
	public String WorkFilterAction_7;
	public String WorkFilterAction_8;
	public String WorkFilterAction_9;
	public String WorkFilterAction2_10;
	public String WorkFilterAction2_11;
	public String WorkFilterAction2_12;
	public String WorkFilterAction2_13;
	public String WorkFilterAction2_14;
	public String WorkFilterAction2_15;
	public String WorkFilterAction2_16;
	public String WorkFilterAction2_5;
	public String WorkFilterAction2_6;
	public String WorkFilterAction2_7;
	public String WorkFilterAction2_8;
	public String WorkFilterAction2_9;
	public String WorkFlowSettingPage_1;
	public String WorkFlowWorkDelivery_0;
	public String WorkflowSynchronizer_2;
	public String WorkHoldon_0;
	public String WorkHoldon_1;
	public String WorkHoldon_2;
	public String WorkInProcess_1;
	public String WorkInProcess_2;
	public String WorkInProcess_3;
	public String WorkInProcess_4;

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