package com.sg.business.management.editor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.WorkTimeProgram;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.IMasterListenerPart;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class CreateWorkTimeProgram extends ChildPrimaryObjectCreator {

	public CreateWorkTimeProgram() {
	}

	@Override
	protected void setParentData(PrimaryObject po) {
		Organization parentOrg = (Organization) po.getParentPrimaryObjectCache();
		// 设置工时方案的所属组织
		parentOrg.makeWorkTimeProgram((WorkTimeProgram) po);
	}

	@Override
	protected String getMessageForEmptySelection() {
		return Messages.get().CreateWorkTimeProgram_0;
	}
	
	@Override
	protected boolean doSaveAfter(PrimaryObjectEditorInput input,
			String operation) {
		// 刷新工时方案列表
		IMasterListenerPart part = (IMasterListenerPart) Widgets
				.getViewPart("management.worktimeprogram"); //$NON-NLS-1$
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
