package com.sg.business.organization.editor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Role;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.IMasterListenerPart;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class RoleCreater extends ChildPrimaryObjectCreator {

	@Override
	protected String getMessageForEmptySelection() {
		return "����Ҫѡ���ϼ���֯����д���";
	}

	@Override
	protected void setParentData(PrimaryObject po) {
		PrimaryObject parentOrg = po.getParentPrimaryObject();
		po.setValue(Role.F_ORGANIZATION_ID, parentOrg.get_id());
	}

	@Override
	protected boolean doSaveAfter(PrimaryObjectEditorInput input,
			String operation) {
		// ˢ��role�б�
		IMasterListenerPart part = (IMasterListenerPart) Widgets
				.getViewPart("organization.role");
		if(part!=null){
			part.reloadMaster();
		}
		return super.doSaveAfter(input, operation);
	}

	@Override
	protected boolean needHostPartListenSaveEvent() {
		return false;
	}

}
