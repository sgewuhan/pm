package com.sg.sales.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.sg.sales.ISalesRole;

public class CompanyRelativeTeamControl extends TeamControl implements ICompanyRelative , ISalesTeam{

	@Override
	public ObjectId getCompanyId() {
		return (ObjectId) getValue(F_COMPANY_ID);
	}
	@Override
	public Company getCompany() {
		return ModelService.createModelObject(Company.class, getCompanyId());
	}
	
	@Override
	public void doInsert(IContext context) throws Exception {
		checkAndDuplicateTeamFrom(getCompany());
		super.doInsert(context);
	}
	
	@Override
	protected String getPermissionRoleNumber() {
		return ISalesRole.CRM_ADMIN_NUMBER;
	}

	@Override
	protected String[] getVisitableFields() {
		return VISIABLE_FIELDS;
	}

	@Override
	protected String[] getDuplicateTeamFields() {
		return VISIABLE_FIELDS;
	}

}
