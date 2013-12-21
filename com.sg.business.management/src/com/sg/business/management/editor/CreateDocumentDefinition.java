package com.sg.business.management.editor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.management.nls.Messages;
import com.sg.business.model.DocumentDefinition;
import com.sg.business.model.Organization;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.IMasterListenerPart;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class CreateDocumentDefinition extends ChildPrimaryObjectCreator {

	@Override
	protected String getMessageForEmptySelection() {
		return Messages.get().CreateDocumentDefinition_0;
	}

	@Override
	protected void setParentData(PrimaryObject po) {
		Organization parentOrg = (Organization) po.getParentPrimaryObjectCache();
		parentOrg.makeDocumentDefinition((DocumentDefinition) po);
	}

	@Override
	protected boolean doSaveAfter(PrimaryObjectEditorInput input,
			String operation) {
		// Ë¢ÐÂroleÁÐ±í
		IMasterListenerPart part = (IMasterListenerPart) Widgets
				.getViewPart("management.documentdefinition"); //$NON-NLS-1$
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