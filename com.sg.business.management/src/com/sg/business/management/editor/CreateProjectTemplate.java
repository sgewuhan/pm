package com.sg.business.management.editor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.IMasterListenerPart;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class CreateProjectTemplate extends ChildPrimaryObjectCreator {

	@Override
	protected String getMessageForEmptySelection() {
		return "����Ҫѡ����֯����д���";
	}

	@Override
	protected void setParentData(PrimaryObject po) {
		Organization parentOrg = (Organization) po.getParentPrimaryObjectCache();
		// ������Ŀģ���������֯
		parentOrg.makeProjectTemplate((ProjectTemplate) po);
	}

	@Override
	protected boolean doSaveAfter(PrimaryObjectEditorInput input,
			String operation) {

		// ˢ���б�
		IMasterListenerPart part = (IMasterListenerPart) Widgets
				.getViewPart("management.projecttemplates");
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
