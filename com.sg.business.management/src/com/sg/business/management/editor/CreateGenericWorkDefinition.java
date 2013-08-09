package com.sg.business.management.editor;

import com.mobnut.db.model.PrimaryObject;
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
		PrimaryObject parentOrg = po.getParentPrimaryObject();
		po.setValue(WorkDefinition.F_ORGANIZATION_ID, parentOrg.get_id());
		po.setValue(WorkDefinition.F__EDITOR,WorkDefinition.EDITOR_GENERIC_WORK);
		po.setValue(WorkDefinition.F_WORK_TYPE, WorkDefinition.WORK_TYPE_GENERIC);
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