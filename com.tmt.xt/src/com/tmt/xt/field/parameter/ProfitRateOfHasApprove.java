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
						.getProcessInstanceVarible("amount",
								new CurrentAccountContext());
				Double profitRate =  (Double) taskForm.getValue("profitrate");
				if(profitRate < 30.0d || amount > 5000000.0d){
					return "ÊÇ";
				} else {
					return "·ñ";
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
