package com.sg.business.commons.editor;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.IInputProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.viewer.ICreateEditorDelegator;
import com.sg.widgets.viewer.ViewerControl;

public class CreateFolderOfProject implements ICreateEditorDelegator {

	@Override
	public IInputProvider create(IStructuredSelection selection,
			String editorId, ViewerControl viewerControl) throws Exception {
		if (selection == null || selection.isEmpty()) {
			throw new Exception(Messages.get().CreateFolderOfProject_0);
		}
		Folder po = ModelService.createModelObject(new BasicDBObject(), Folder.class);
		
		PrimaryObject parent = (PrimaryObject) selection.getFirstElement();
		ObjectId parentId;
		ObjectId rootId;
		ObjectId projectId;
		String containerCollection, containerDB;

		if (parent instanceof Folder) {
			parentId = parent.get_id();
			rootId = ((Folder) parent).getRoot_id();
			projectId = ((Folder) parent).getProject_id();
			containerDB = parent.getDbName();
			containerCollection = IModelConstants.C_ORGANIZATION;
			po.setValue(Folder.F_PARENT_ID, parentId);
			po.setValue(Folder.F_ROOT_ID, rootId);
			po.setValue(Folder.F_CONTAINER_DB, containerDB);
			po.setValue(Folder.F_CONTAINER_COLLECTION, containerCollection);
			po.setValue(Folder.F_PROJECT_ID, projectId);
		} 
		
		DataObjectDialog.openDialog(po, editorId, true, null);
		viewerControl.refreshViewer();
		return null;
	}
	
}
