package com.sg.business.model.dataset.organization;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.Role;
import com.sg.business.model.Work;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class OrgUserCascade extends MasterDetailDataSetFactory {


	public OrgUserCascade() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	@Override
	protected String getDetailCollectionKey() {
		return Organization.F__ID;
	}

	@Override
	protected Object getMasterValue() {
		 ObjectId _id = (ObjectId) master.getValue(Work.F_ASSIGNMENT_CHARGER_ROLE_ID);
		 ProjectRole projectRole = ModelService.createModelObject(ProjectRole.class, _id);
		 Role role = projectRole.getOrganizationRole();
		 if(role != null){
			 return role.getOrganization_id();
		 }else{
			 return null;
		 }
	}

}
