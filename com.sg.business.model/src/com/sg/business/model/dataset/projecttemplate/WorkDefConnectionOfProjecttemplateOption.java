package com.sg.business.model.dataset.projecttemplate;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

public class WorkDefConnectionOfProjecttemplateOption extends
		OptionDataSetFactory {

	public WorkDefConnectionOfProjecttemplateOption() {
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
	}

	@Override
	public void setEditorData(PrimaryObject data) {
		Object projectTemplateId = data
				.getValue(WorkDefinition.F_PROJECT_TEMPLATE_ID);
		setQueryCondition(new BasicDBObject().append(
				WorkDefinition.F_PROJECT_TEMPLATE_ID,
				projectTemplateId));
	}

}
