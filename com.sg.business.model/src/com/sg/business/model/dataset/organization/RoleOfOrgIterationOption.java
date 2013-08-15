package com.sg.business.model.dataset.organization;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.RoleDefinition;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

public class RoleOfOrgIterationOption extends OptionDataSetFactory {

	public RoleOfOrgIterationOption() {
		super(IModelConstants.DB, IModelConstants.C_ROLE_DEFINITION);
	}

	@Override
	public void setEditorData(PrimaryObject data) {
		WorkDefinition workd = (WorkDefinition) data;
		ProjectTemplate projectTemplate = workd.getProjectTemplate();
		setQueryCondition(new BasicDBObject().append(
				RoleDefinition.F_PROJECT_TEMPLATE_ID, projectTemplate.get_id()));
	}

}
