package com.sg.sales.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;

public class CompanyRelativeTeamControl extends TeamControl implements ICompanyRelative {

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
		TeamControl.checkTeam(getCompany(),this);
		super.doInsert(context);
	}

}
