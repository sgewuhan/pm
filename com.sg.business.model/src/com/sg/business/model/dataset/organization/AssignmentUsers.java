package com.sg.business.model.dataset.organization;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class AssignmentUsers extends SingleDBCollectionDataSetFactory {

	
	private String useid;

	public AssignmentUsers() {
		super(IModelConstants.DB, IModelConstants.C_USER);
		useid=new CurrentAccountContext().getConsignerId();
	}
	
	@Override
	public DataSet getDataSet() {
		
		User user = UserToolkit.getUserById(useid);
		List<PrimaryObject> userList=new ArrayList<PrimaryObject>();
		userList.add(user);
		List<PrimaryObject> roles = user.getRoles(Role.ROLE_ASSIGNMENT_ID);
		for(PrimaryObject po:roles){
			Organization organization = ((Role)po).getOrganization();
			userList.addAll(organization.getUser());
		}
		return new DataSet(userList);
	}

}
