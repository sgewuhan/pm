package com.tmt.gs.field.parameter;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.field.processparameter.AbstractRoleParameterDelegator;
import com.sg.business.model.Organization;
import com.sg.business.taskforms.IRoleConstance;

public class GSDataAudit extends AbstractRoleParameterDelegator {

	@Override
	protected String getRoldNumber(Object type) {
		return IRoleConstance.ROLE_DATAAUDIT_ID;
	}

	@Override
	protected int getSelectType(Object type) {
		return Organization.ROLE_SEARCH_UP;
	}

	@Override
	protected Organization getOrganization(String processParameter,
			String taskDatakey, PrimaryObject taskFormData) {
		Object value = taskFormData.getValue(taskDatakey);
		if (value instanceof ObjectId) {
			return ModelService.createModelObject(Organization.class,
					(ObjectId) value);
		}
		return null;
	}

	@Override
	protected Object setType(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		return null;
	}

}
