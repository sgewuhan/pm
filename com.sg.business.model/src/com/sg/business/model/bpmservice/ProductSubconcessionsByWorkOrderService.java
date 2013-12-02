package com.sg.business.model.bpmservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Work;

public class ProductSubconcessionsByWorkOrderService extends ServiceProvider {

	public ProductSubconcessionsByWorkOrderService() {
	}

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			try {
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				String processId = (String) host.getValue("processId");
				String processName = (String) host.getValue("processName");
				if (host instanceof Work) {
					Work work = (Work) host;
					// 根据工作令号获取项目
					Project project = work.getProject();
					if (project == null) {
						String workorder = (String) getInputValue("workorder");
						DBCollection col = DBActivator.getCollection(
								IModelConstants.DB, IModelConstants.C_PROJECT);
						DBObject dbo = col.findOne(new BasicDBObject().append(
								Project.F_WORK_ORDER, workorder));
						if (dbo != null) {
							project = ModelService.createModelObject(dbo,
									Project.class);
						}
					}
					// 商品转批
					if (project != null) {
						Object product = host.getValue("product");
						if (product instanceof List<?>) {
							List<?> productList = (List<?>) product;
							project.doChangeMassProduction(new BPMServiceContext(
									processName, processId), productList);
						}
					}

				} else {
					result.put("returnCode", "ERROR");
					result.put("returnMessage", "商品转批出现错误!");
				}
			} catch (Exception e) {
				result.put("returnCode", "ERROR");
				result.put("returnMessage", e.getMessage());
			}
		}
		return result;
	}

}
