package com.tmt.xt.field.parameter;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;
import com.sg.business.model.TaskForm;
import com.sg.widgets.part.CurrentAccountContext;

public class ProfitRateOfHasApprove implements IProcessParameterDelegator {

	public ProfitRateOfHasApprove() {
	}

	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			try {
				Double amount = (Double) taskForm
						.getProcessInstanceVarible("amount", //$NON-NLS-1$
								new CurrentAccountContext());
				Double profitRate =  (Double) taskForm.getValue("profitrate"); //$NON-NLS-1$
				if(profitRate < 30.0d || amount > 5000000.0d){
					return "ÊÇ"; //$NON-NLS-1$
				} else {
					return "·ñ"; //$NON-NLS-1$
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
