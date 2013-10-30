package com.tmt.kfzx.handler;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IProgressMonitor;

import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.sg.business.model.Container;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.IDataObjectDialogCallback;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class MeetingReviewCommentfile implements
IDataObjectDialogCallback {

	public MeetingReviewCommentfile() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean doSaveBefore(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		return true;
	}

	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		TaskForm taskform = (TaskForm) input.getData();
		Work work = taskform.getWork();
		if (work != null) {
			Project project = work.getProject();
			if (project != null) {
				IContext context = new CurrentAccountContext();
				ObjectId documentId;
				Document document;

				AccountInfo accountInfo = context.getAccountInfo();
				BasicDBObject condition = new BasicDBObject();
				condition = new BasicDBObject();
				condition.put(Deliverable.F_PROJECT_ID, project.get_id());
				condition.put(Deliverable.F_WORK_ID, work.get_id());
				String deliverableName = input.getEditorConfigurator()
						.getName();
				deliverableName = deliverableName + " "
						+ accountInfo.getUserName();
				condition.put(Deliverable.F_DESC, deliverableName);

				List<PrimaryObject> deliverableList = project
						.getRelationByCondition(Deliverable.class, condition);
				if (deliverableList != null && deliverableList.size() > 0) {
					Deliverable deliverable = (Deliverable) deliverableList
							.get(0);
					documentId = deliverable.getDocumentId();
					document = ModelService.createModelObject(Document.class, documentId);
				} else {
					ObjectId folderId;
					BasicDBObject folderCondition = new BasicDBObject();
					folderCondition.put(Folder.F_IS_WORKFLOW_FOLDER, Boolean.TRUE);
					folderCondition.put(Folder.F_DESC, work.getDesc());
					folderCondition.put(Folder.F_IS_PROJECT_FOLDERROOT, Boolean.FALSE);
					folderCondition.put(Folder.F_PROJECT_ID, project.get_id());
					folderCondition.put(Folder.F_ROOT_ID, project.getFolderRootId());
					folderCondition.put(Folder.F_PARENT_ID, project.getFolderRootId());
					
					List<PrimaryObject> folderList = project
							.getRelationByCondition(Folder.class, folderCondition);
					if (folderList != null && folderList.size() > 0) {
						folderId = folderList.get(0).get_id();
					} else {
						String containerCollection, containerDB;
						containerCollection = IModelConstants.C_ORGANIZATION;
						Container container = Container.adapter(project,
								Container.TYPE_ADMIN_GRANTED);
						containerDB = (String) container
								.getValue(Container.F_SOURCE_DB);
						folderCondition.put(Folder.F_CONTAINER_DB, containerDB);
						folderCondition.put(Folder.F_CONTAINER_COLLECTION,
								containerCollection);

						Folder wfFolder = ModelService.createModelObject(folderCondition,
								Folder.class);
						wfFolder.doInsert(context);
						folderId = wfFolder.get_id();
					}
					
					BasicDBObject documentcondition = new BasicDBObject();
					documentcondition.put(Document.F_DESC, deliverableName);
					documentcondition.put(Document.F_PROJECT_ID,
							project.get_id());
					document = ModelService.createModelObject(
							documentcondition, Document.class);
					document.setValue(Document.F_FOLDER_ID, folderId);
					document.doSave(context);
					documentId = document.get_id();
					
					condition.put(Deliverable.F_MANDATORY, Boolean.TRUE);
					condition.put(Deliverable.F_DOCUMENT_ID, documentId);
					Deliverable deliverable = ModelService.createModelObject(
							condition, Deliverable.class);
					deliverable.doInsert(context);
				}
				Object commentfile = taskform.getValue("commentfile");
				if(commentfile != null && commentfile instanceof BasicDBList){
					BasicDBList commentfileList = (BasicDBList) commentfile;
					Object inputValue = document.getValue(Document.F_VAULT, true);
					if (inputValue != null && inputValue instanceof BasicDBList) {
						BasicDBList vault = (BasicDBList) inputValue;
						vault.addAll(commentfileList);
						document.setValue(Document.F_VAULT, vault);
						document.doUpdate(context);
					} else{
						document.setValue(Document.F_VAULT, commentfileList);
						document.doUpdate(context);
					}
				}
			}

		}
		return true;
	}

	@Override
	public boolean okPressed() {
		return false;
	}

	@Override
	public void cancelPressed() {

	}

	@Override
	public boolean needSave() {
		return true;
	}

}

