package com.sg.business.management.editor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.FolderDefinition;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.IMasterListenerPart;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class CreateFolder extends ChildPrimaryObjectCreator {

	public CreateFolder() {
	}

	@Override
	protected void setParentData(PrimaryObject po) {
		FolderDefinition folderd = (FolderDefinition) po.getParentPrimaryObjectCache();
		folderd.makeFolderDefinition((FolderDefinition) po);
	}

	@Override
	protected String getMessageForEmptySelection() {
		return Messages.get().CreateFolderTemplate_0;
	}
	
	@Override
	protected boolean doSaveAfter(PrimaryObjectEditorInput input,
			String operation) {
		IMasterListenerPart part = (IMasterListenerPart) Widgets
				.getViewPart("management.foldertemplate"); //$NON-NLS-1$
		if (part != null) {
			part.reloadMaster();
		}
		return super.doSaveAfter(input, operation);
	}
	
	@Override
	protected boolean needHostPartListenSaveEvent() {
		return false;
	}

}
