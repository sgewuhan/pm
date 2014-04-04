package com.sg.business.model.bpmservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;

public class ChangePlanService extends ServiceProvider {

	public ChangePlanService() {
	}

	@Override
	public Map<String, Object> run(Object arg0) {

		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content"); //$NON-NLS-1$
		Object value;
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof Work) {
				Work changeplan = (Work) host;
				DBObject processData = WorkflowUtils
						.getProcessInfoFromJSON(jsonContent);
				String processId = (String) processData.get("processId"); //$NON-NLS-1$
				String processName = (String) processData.get("processName"); //$NON-NLS-1$
				IContext context = new BPMServiceContext(processName, processId);

				value = getInputValue("ecn"); //$NON-NLS-1$
				List<Work> ecnList = getInputVauleList(value);
				for (Work ecn : ecnList) {
					String ecapapa = (String) ecn
							.getValue(Work.F_INTERNAL_ECAPARA);
					if (ecapapa != null && ecapapa.equals(getOperation())) {
						if (!ecn.isPersistent()) {
							ObjectId palnid = changeplan.get_id();
							ecn.setValue(Work.F_PARENT_ID, palnid);
							ecn.setValue(Work.F_SETTING_CAN_BREAKDOWN, Boolean.TRUE);
							try {
								ecn.doSave(context);
							} catch (Exception e) {
								MessageUtil.showToast(e);
							}
						}
						value = getInputValue(ecapapa);
						List<Work> ecaList = getInputVauleList(value);
						createECA(ecaList, ecn, context);
						try {
							ecn.doStart(context);
						} catch (Exception e) {
							MessageUtil.showToast(e);
						}
					}
					

				}

				Object changetype = changeplan.getValue(Work.F_INTERNAL_TYPE);
				if (!(Work.INTERNAL_TYPE_CHANGE.equals(changetype))) {
					try {
						changeplan.setValue(Work.F_INTERNAL_TYPE,
								Work.INTERNAL_TYPE_CHANGE);
						changeplan.doSave(context);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}

				}

			}

		}
		return result;
	}

	private List<Work> getInputVauleList(Object value) {
		List<Work> workList = new ArrayList<Work>();
		if (value instanceof List<?>) {
			List<?> list = (List<?>) value;
			for (Object dbo : list) {
				Work ecn = ModelService.createModelObject((DBObject) dbo,
						Work.class);
				workList.add(ecn);
			}
		} else if (value instanceof Object[]) {
			Object[] array = (Object[]) value;
			for (Object dbo : array) {
				Work ecn = ModelService.createModelObject((DBObject) dbo,
						Work.class);
				workList.add(ecn);
			}
		}
		return workList;
	}

	private void createECA(List<Work> ecaList, Work ecn, IContext context) {
		for (Work eca : ecaList) {
			if (!eca.isPersistent()) {
				List<Deliverable> deliverableList = new ArrayList<Deliverable>();
				// 获取变更活动的交付物
				Object object = eca.getValue(Work.TEMPLATE_DELIVERABLE);
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
//					eca.setValue(Work.TEMPLATE_DELIVERABLE, null);
					// eca.setValue(Work.F_MANDATORY,
					// Boolean.TRUE);
					// eca.setValue(Work.F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH,
					// Boolean.FALSE);
					Date start = (Date) eca.getPlanStart();
					Date finish = (Date) eca.getPlanFinish();
					if (start == null ||finish == null) {
						eca.setValue(Work.F_PLAN_START, ecn.getPlanStart());
						eca.setValue(Work.F_PLAN_FINISH, ecn.getPlanFinish());
					}
					eca.setValue(Work.F_PARENT_ID, ecn.get_id());
					eca.setValue(Work.F_WORK_TYPE, Work.WORK_TYPE_STANDLONE);
					eca.doSave(context);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}

				for (Deliverable deliverable : deliverableList) {
					try {
						deliverable.setValue(Deliverable.F_WORK_ID,
								eca.get_id());
						Document document = deliverable.getDocument();
						document.doUpdateVersion();
						deliverable.doSave(context);

					} catch (Exception e) {
						MessageUtil.showToast(e);
					}
				}

			}
		}
	}
}
