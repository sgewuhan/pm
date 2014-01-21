package com.sg.business.management.editor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.WorkDefinition;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.IMasterListenerPart;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class CreateStandloneWorkDefinition extends ChildPrimaryObjectCreator {

	@Override
	protected String getMessageForEmptySelection() {
		return Messages.get().CreateStandloneWorkDefinition_0;
	}

	@Override
	protected void setParentData(PrimaryObject po) {
		Organization parentOrg = (Organization) po.getParentPrimaryObjectCache();
		// 设置项目模板的所属组织
		parentOrg.makeStandardWorkDefinition((WorkDefinition) po);
	}

	@Override
	protected boolean doSaveAfter(PrimaryObjectEditorInput input,
			String operation) {
		// 刷新role列表
		IMasterListenerPart part = (IMasterListenerPart) Widgets
				.getViewPart("management.standlonework.definitions"); //$NON-NLS-1$
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