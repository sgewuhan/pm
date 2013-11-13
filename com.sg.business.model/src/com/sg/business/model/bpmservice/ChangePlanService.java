package com.sg.business.model.bpmservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;

public class ChangePlanService extends ServiceProvider {

	public ChangePlanService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, Object> run(Object arg0) {

		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof Work) {
				DBObject processData = WorkflowUtils
						.getProcessInfoFromJSON(jsonContent);
				String processId = (String) processData.get("processId");
				String processName = (String) processData.get("processName");
				IContext context = new BPMServiceContext(processName, processId);

				List<Work> workList = new ArrayList<Work>();
				Object value = getInputValue("ecn");
				if (value instanceof List<?>) {
					List<?> list = (List<?>) value;
					for (Object dbo : list) {
						Work work = ModelService.createModelObject(
								(DBObject) dbo, Work.class);
						workList.add(work);
					}
				} else if (value instanceof Object[]) {
					Object[] array = (Object[]) value;
					for (Object dbo : array) {
						Work work = ModelService.createModelObject(
								(DBObject) dbo, Work.class);
						workList.add(work);
					}

				}

				for (Work work : workList) {
					if (!work.isPersistent()) {
						List<Deliverable> deliverableList = new ArrayList<Deliverable>();
						Object object = work
								.getValue(Work.TEMPLATE_DELIVERABLE);
						if (object instanceof List<?>) {
							List<?> list = (List<?>) object;
							for (Object dbo : list) {
								Deliverable deliverable = ModelService
										.createModelObject((DBObject) dbo,
												Deliverable.class);
								deliverableList.add(deliverable);
							}
						} else if (object instanceof Object[]) {
							Object[] array = (Object[]) object;
							for (Object dbo : array) {
								Deliverable deliverable = ModelService
										.createModelObject((DBObject) dbo,
												Deliverable.class);
								deliverableList.add(deliverable);
							}

						}
						try {
							work.setValue(Work.TEMPLATE_DELIVERABLE, null);
							work.doSave(context);
						} catch (Exception e) {
							MessageUtil.showToast(e);
						}

						for (Deliverable deliverable : deliverableList) {
							try {
								
								deliverable.setValue(Deliverable.F_WORK_ID, work.get_id());
								deliverable.doSave(context);

							} catch (Exception e) {
								MessageUtil.showToast(e);
							}
						}

					}
				}

			}
		}
		return result;
	}

}
