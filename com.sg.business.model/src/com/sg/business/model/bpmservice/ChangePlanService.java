package com.sg.business.model.bpmservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.part.CurrentAccountContext;

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
				
				for(Work work:workList){
					List<Deliverable> deliverableList = new ArrayList<Deliverable>();
					Object object = work.getValue(Work.TEMPLATE_DELIVERABLE);
					if (object instanceof List<?>) {
						List<?> list = (List<?>) object;
						for (Object dbo : list) {
							Deliverable deliverable = ModelService.createModelObject(
									(DBObject) dbo, Deliverable.class);
							deliverableList.add(deliverable);
						}
					} else if (object instanceof Object[]) {
						Object[] array = (Object[]) object;
						for (Object dbo : array) {
							Deliverable deliverable = ModelService.createModelObject(
									(DBObject) dbo, Deliverable.class);
							deliverableList.add(deliverable);
						}

					}
					
					for(Deliverable deliverable:deliverableList){
						Document document = deliverable.getDocument();
						System.out.println(document.getDesc());
					}
				}

				// List<String> workdlist = (List<String>)
				// getInputValue("standlonework_list");
				// for (String id : workdlist) {
				// ObjectId workdid = new ObjectId(id);
				// WorkDefinition standloneworkd = ModelService
				// .createModelObject(WorkDefinition.class, workdid);
				// try {
				// IContext context = new BPMServiceContext(processName,
				// processId);
				// Work standloneWork = createStandloneWork(
				// standloneworkd, context);
				// standloneWork.doStart(context);
				//
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// }

			}
		}
		return result;
	}

	private Work createStandloneWork(WorkDefinition standloneworkd,
			IContext context) throws Exception {

		// Work standloneWork = standloneworkd.makeStandloneWork(null,context);

		Work work = ModelService.createModelObject(Work.class);
		// work.setValue(Work.F__ID, new ObjectId());
		// work.setValue(Work.F_WORK_TYPE, Work.WORK_TYPE_STANDLONE);
		// work.setValue(Work.F_LIFECYCLE, Work.STATUS_ONREADY_VALUE);
		// // 设置工作的描述字段
		// Object value = standloneworkd.getValue(WorkDefinition.F_DESC);
		// if (value != null) {
		// work.setValue(Work.F_DESC, value);
		// }
		// value = standloneworkd.getValue(WorkDefinition.F_DESC_EN);
		// if (value != null) {
		// work.setValue(Work.F_DESC_EN, value);
		// }
		// // 设置工作流
		// value = standloneworkd.getValue(IWorkCloneFields.F_WF_EXECUTE);
		// if (value != null) {
		// work.setValue(IWorkCloneFields.F_WF_EXECUTE, value);
		// }
		// // 设置执行工作流是否激活
		// value = standloneworkd
		// .getValue(IWorkCloneFields.F_WF_EXECUTE_ACTIVATED);
		// if (value != null) {
		// work.setValue(IWorkCloneFields.F_WF_EXECUTE_ACTIVATED, value);
		// }

		// standloneWork.setValue(Work.F_CHARGER,);// 设置负责人
		// work.doChangeDeliverableLifeCycleStatus(context,
		// Document.STATUS_WORKING_ID);

		return work;
	}

}
