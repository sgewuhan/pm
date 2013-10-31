package com.sg.business.model.bpmservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Work;

public class WorkToProjectService extends ServiceProvider {

	public WorkToProjectService() {
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
					Project project = null;
					String workorder = (String) getInputValue("number");
					DBCollection col = DBActivator.getCollection(
							IModelConstants.DB, IModelConstants.C_PROJECT);
					DBObject dbo = col.findOne(new BasicDBObject().append(
							Project.F_WORK_ORDER, workorder));
					if (dbo != null) {
						project = ModelService.createModelObject(dbo,
								Project.class);
					}
					// 附加工作至项目中
					if (project != null) {
						ObjectId project_id = project.get_id();
						Folder folderRoot = project.getFolderRoot();
						Work rootwork = project.getWBSRoot();
						int maxChildSeq = rootwork.getMaxChildSeq();
						work.setValue(Work.F_PARENT_ID, rootwork.get_id());
						work.setValue(Work.F_PROJECT_ID, project_id);
						work.setValue(Work.F_SEQ, maxChildSeq+1);
						work.doSave((new BPMServiceContext(processName,
								processId)));

						// 文档归档
						List<PrimaryObject> deliverable = work.getDeliverable();
						for (PrimaryObject del : deliverable) {
							del.setValue(Deliverable.F_PROJECT_ID, project_id);
							List<PrimaryObject> deliverableDocuments = work
									.getDeliverableDocuments();
							for (PrimaryObject doc : deliverableDocuments) {
								doc.setValue(Document.F_PROJECT_ID, project_id);
								doc.setValue(Document.F_FOLDER_ID,
										folderRoot.get_id());
								doc.doSave((new BPMServiceContext(processName,
										processId)));
							}
							del.doSave((new BPMServiceContext(processName,
									processId)));

						}
					}

				} else {
					result.put("returnCode", "ERROR");
					result.put("returnMessage", "附加工作出现错误!");
				}
			} catch (Exception e) {
				result.put("returnCode", "ERROR");
				result.put("returnMessage", e.getMessage());
			}
		}
		return result;
	}

}
