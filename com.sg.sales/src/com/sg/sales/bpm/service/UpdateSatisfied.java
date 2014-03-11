package com.sg.sales.bpm.service;

import java.util.HashMap;
import java.util.Map;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.BackgroundContext;

public class UpdateSatisfied extends ServiceProvider {

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();

		Object content = getInputValue("content"); //$NON-NLS-1$
		if (content instanceof String) {
			String jsonContent = (String) content;
			try {
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if(host instanceof Work){
				Work work = (Work) host;
				work = ModelService.createModelObject(Work.class, work.get_id());
				work.setValue(Work.F_ACHIEVED, Boolean.FALSE);
				work.doSave(new BackgroundContext());
				}
			}catch(Exception e){
				result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
				result.put("returnMessage", Messages.get().WorkToProjectByWorkOrderService_0); //$NON-NLS-1$
			}
		}
		return result;
	}

}
