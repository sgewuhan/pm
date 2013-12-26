package com.sg.business.model.bpmservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Document;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;

public class ArchiveDocument extends ServiceProvider {

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();

		Object content = getInputValue("content"); //$NON-NLS-1$
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof Work) {
				Work work = (Work) host;
				DBObject processData = WorkflowUtils
						.getProcessInfoFromJSON(jsonContent);
				String processId = (String) processData.get("processId"); //$NON-NLS-1$
				String processName = (String) processData.get("processName"); //$NON-NLS-1$
				Organization org = getArchiveOrganization();
				if (org != null) {
					Project project = work.getProject();
					if (project == null) {
						IContext context = new BPMServiceContext(processName,
								processId);
						Folder folder = org.makeFolder(context, work);
						try {
							folder.doInsert(context);
						} catch (Exception e) {
							result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
							result.put("returnMessage", e.getMessage()); //$NON-NLS-1$
						}
						List<ObjectId> documentIds = work
								.getOutputDeliverableDocumentIds();
						DBCollection col = DBActivator.getCollection(
								IModelConstants.DB, IModelConstants.C_DOCUMENT);
						col.update(
								new BasicDBObject().append(Document.F__ID,
										new BasicDBObject().append("$in",
												documentIds)),
								new BasicDBObject().append("$set",
										new BasicDBObject().append(
												Document.F_FOLDER_ID,
												folder.get_id())), false, true);
					} else {
						ObjectId rootId = project.getFolderRootId();
						List<ObjectId> documentIds = work
								.getOutputDeliverableDocumentIds();
						DBCollection col = DBActivator.getCollection(
								IModelConstants.DB, IModelConstants.C_DOCUMENT);
						col.update(
								new BasicDBObject().append(Document.F__ID,
										new BasicDBObject().append("$in",
												documentIds)),
								new BasicDBObject().append("$set",
										new BasicDBObject().append(
												Document.F_FOLDER_ID, rootId)),
								false, true);
					}
				} else {
					result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
					result.put("returnMessage", "该无法找到可以进行归档的文档容器"); //$NON-NLS-1$
				}
			}
		}
		return result;
	}

	private Organization getArchiveOrganization() {
		String launcher = (String) getInputValue("act_rule_launcher");
		if (launcher != null) {
			User user = UserToolkit.getUserById(launcher);
			Organization org = user.getOrganization();
			if (org != null) {
				return org.getContainerOrganization();
			}
		}
		return null;
	}

}
