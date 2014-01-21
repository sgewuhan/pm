package com.sg.business.vault.editor;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Document;
import com.sg.business.model.Folder;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class DocumentCreater extends ChildPrimaryObjectCreator {

	private static final String ERROR_MSG = Messages.get().DocumentCreater_0;

	@Override
	protected String getMessageForEmptySelection() {
		return ERROR_MSG;
	}

	@Override
	protected void checkSelection(IStructuredSelection selection)
			throws Exception {
		Object parent = selection.getFirstElement();
		if (!(parent instanceof Folder)) {
			throw new Exception(ERROR_MSG);
		}
	}

	@Override
	protected void setParentData(PrimaryObject po) {
		PrimaryObject parent = po.getParentPrimaryObjectCache();
		if (parent instanceof Folder) {
			ObjectId parentId = parent.get_id();
			po.setValue(Document.F_FOLDER_ID, parentId);
		}

	}

}
