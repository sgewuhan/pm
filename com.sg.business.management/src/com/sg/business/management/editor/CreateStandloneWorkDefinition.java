package com.sg.business.management.editor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.IMasterListenerPart;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class CreateStandloneWorkDefinition extends ChildPrimaryObjectCreator {

	@Override
	protected String getMessageForEmptySelection() {
		return "您需要选择组织后进行创建";
	}

	@Override
	protected void setParentData(PrimaryObject po) {
		PrimaryObject parentOrg = po.getParentPrimaryObject();
		po.setValue(WorkDefinition.F_ORGANIZATION_ID, parentOrg.get_id());
		po.setValue(WorkDefinition.F__EDITOR,WorkDefinition.EDITOR_STANDLONE_WORK);
		po.setValue(WorkDefinition.F_WORK_TYPE, WorkDefinition.WORK_TYPE_STANDLONE);
	}

	@Override
	protected boolean doSaveAfter(PrimaryObjectEditorInput input,
			String operation) {
		// 刷新role列表
		IMasterListenerPart part = (IMasterListenerPart) Widgets
				.getViewPart("management.standlonework.definitions");
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