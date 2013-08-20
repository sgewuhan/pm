package com.sg.business.model.dataset.project;

import org.bson.types.ObjectId;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

public class ProjectTemplateOfOrg extends OptionDataSetFactory {

	public ProjectTemplateOfOrg() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
	}

	@Override
	public void setEditorData(PrimaryObject data) {
		Project project = (Project) data;
		ObjectId org_id = project.getFunctionOrganizationId();
		if(org_id!=null){
			setQueryCondition(new BasicDBObject().append(ProjectTemplate.F_ORGANIZATION_ID, org_id).append(ProjectTemplate.F_ACTIVATED, Boolean.TRUE));
		}else{
			setQueryCondition(new BasicDBObject().append(ProjectTemplate.F__ID, null));
		}
		super.setEditorData(data);
	}


}
