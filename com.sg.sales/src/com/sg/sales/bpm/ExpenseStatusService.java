package com.sg.sales.bpm;

import java.util.HashMap;
import java.util.Map;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.sales.model.ISalesWork;
import com.sg.sales.model.WorkCost;

public class ExpenseStatusService extends ServiceProvider {

	public ExpenseStatusService() {
	}

	@Override
	public Map<String, Object> run(Object parameter) {
		String operation = getOperation();

		HashMap<String, Object> result = new HashMap<String, Object>();

		Object content = getInputValue("content"); //$NON-NLS-1$
		Object actor = getInputValue("actor");
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof Work) {
				Work work = (Work) host;
				if (ISalesWork.WORK_CATAGORY_SALES_APPLYEXPENSE.equals(work
						.getValue(Work.F_WORK_CATAGORY))) {
					try {
						WorkCost.changeCostStatus(work, operation,(String) actor);
					} catch (Exception e) {
						result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
						result.put("returnMessage", e.getMessage()); //$NON-NLS-1$
					}
				}
			}
		}
		return result;
	}

}
