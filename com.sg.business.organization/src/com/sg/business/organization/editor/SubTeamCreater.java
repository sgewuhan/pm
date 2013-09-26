package com.sg.business.organization.editor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;


public class SubTeamCreater extends ChildPrimaryObjectCreator{

	@Override
	protected String getMessageForEmptySelection() {
		return "����Ҫѡ���ϼ���֯����д���";
	}

	@Override
	protected void setParentData(PrimaryObject po) {
		Organization parentOrg = (Organization) po.getParentPrimaryObjectCache();
		parentOrg.makeChildOrganization((Organization) po);
	}

}
