package com.sg.business.management.editor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.IMasterListenerPart;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class CreateGenericWorkDefinition extends ChildPrimaryObjectCreator {

	@Override
	protected String getMessageForEmptySelection() {
		return "����Ҫѡ����֯����д���";
	}

	@Override
	protected void setParentData(PrimaryObject po) {
		Organization parentOrg = (Organization) po.getParentPrimaryObjectCache();
		parentOrg.makeGenericWorkDefinition((WorkDefinition) po);
	}

	@Override
	protected boolean doSaveAfter(PrimaryObjectEditorInput input,
			String operation) {
		// ˢ��role�б�
		IMasterListenerPart part = (IMasterListenerPart) Widgets
				.getViewPart("management.genericwork.definitions");
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