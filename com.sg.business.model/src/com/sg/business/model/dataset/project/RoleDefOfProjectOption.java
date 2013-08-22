package com.sg.business.model.dataset.project;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.Work;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

public class RoleDefOfProjectOption extends OptionDataSetFactory {

	public RoleDefOfProjectOption() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT_ROLE);
	}

	@Override
	public void setEditorData(PrimaryObject data) {
		Work workd = (Work) data;
		Project project = workd.getProject();
		setQueryCondition(new BasicDBObject().append(
				ProjectRole.F_PROJECT_ID, project.get_id()));
	}

}
