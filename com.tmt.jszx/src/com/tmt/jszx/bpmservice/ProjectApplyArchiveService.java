package com.tmt.jszx.bpmservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Container;
import com.sg.business.model.Document;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.BPMServiceContext;
import com.tmt.jszx.nls.Messages;

public class ProjectApplyArchiveService extends ServiceProvider {

	public ProjectApplyArchiveService() {
	}

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content"); //$NON-NLS-1$
		if (content instanceof String) {
			String jsonContent = (String) content;
			try {
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				DBObject processData = WorkflowUtils
						.getProcessInfoFromJSON(jsonContent);
				String processId = (String) processData.get("processId"); //$NON-NLS-1$
				String processName = (String) processData.get("processName"); //$NON-NLS-1$
				if (host instanceof Work) {
					Work work = (Work) host;
					String dept=(String) getInputValue("dept"); //$NON-NLS-1$
					ObjectId orgId=new ObjectId(dept);
						if (orgId != null) {
							Organization org = ModelService.createModelObject(
									Organization.class, orgId);
							ObjectId containerOrgId = org
									.getContainerOrganizationId();
							if (containerOrgId != null) {
								String filderNmae = "" //$NON-NLS-1$
										+ work.getProject().getDesc();
								filderNmae = filderNmae + "-" + work.getDesc(); //$NON-NLS-1$
								ObjectId folderId;
								BasicDBObject folderCondition = new BasicDBObject();
								folderCondition.put(Folder.F_IS_WORKFLOW_FOLDER, Boolean.FALSE);
								folderCondition.put(Folder.F_DESC, filderNmae);
								folderCondition.put(Folder.F_IS_PROJECT_FOLDERROOT, Boolean.FALSE);
								folderCondition.put(Folder.F_ROOT_ID, containerOrgId);
								String containerCollection, containerDB;
								containerCollection = IModelConstants.C_ORGANIZATION;
								Container container = Container.adapter(work.getProject(),
										Container.TYPE_ADMIN_GRANTED);
								containerDB = (String) container
										.getValue(Container.F_SOURCE_DB);
								folderCondition.put(Folder.F_CONTAINER_DB, containerDB);
								folderCondition.put(Folder.F_CONTAINER_COLLECTION,
										containerCollection);
								Folder wfFolder = ModelService.createModelObject(folderCondition,
										Folder.class);
								wfFolder.doInsert(new BPMServiceContext(processName, processId));
								folderId = wfFolder.get_id();
								
								List<PrimaryObject> documentList = work.getDeliverableDocuments();
								for (PrimaryObject po : documentList) {
									if(po instanceof Document){
										Document document = (Document) po;
										BasicDBObject newDocument = new BasicDBObject();
										newDocument.put(Document.F__SUMMARY, document.getValue(Document.F__SUMMARY));
										newDocument.put(Document.F_DESCRIPTION, document.getValue(Document.F_DESCRIPTION));
										newDocument.put(Document.F_DOCUMENT_NUMBER, document.getValue(Document.F_DOCUMENT_NUMBER));
										newDocument.put(Document.F_DOCUMENT_TYPE, document.getValue(Document.F_DOCUMENT_TYPE));
										newDocument.put(Document.F_FOLDER_ID, folderId);
										newDocument.put(Document.F_VAULT, document.getValue(Document.F_VAULT));
										newDocument.put(Document.F_WORK_ID, document.getValue(Document.F_WORK_ID));
										newDocument.put(Document.F_PROJECT_ID, document.getValue(Document.F_PROJECT_ID));
										Document newdocument = ModelService.createModelObject(newDocument, Document.class);
										newdocument.doSave(new BPMServiceContext(processName, processId));
									}
								}
								
							} else {
								result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
								result.put("returnMessage", Messages.get().ProjectApplyArchiveService_0); //$NON-NLS-1$
							}
						}
					}
			} catch (Exception e) {
				result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
				result.put("returnMessage", e.getMessage()); //$NON-NLS-1$
			}
		}
		return result;
	}

}
