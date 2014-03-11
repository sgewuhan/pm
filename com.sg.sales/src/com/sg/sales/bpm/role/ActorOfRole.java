package com.sg.sales.bpm.role;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.service.actor.IActorIdProvider;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;

public abstract class ActorOfRole implements IActorIdProvider {

	public ActorOfRole() {
	}

	@Override
	public String getActorId(Object[] input) {
		Work work = (Work) input[0];
		User user = work.getCharger();
		if(user!=null){
			Organization org = user.getOrganization();
			if(org!=null){
				Role role = org.getRole(getRoleId(), getSearchMethod());
				if (role != null) {
					List<PrimaryObject> assignment = role.getAssignment();
					if (assignment != null && assignment.size() > 0) {
						return ((RoleAssignment) assignment.get(0)).getUserid();
					}
				}
			}
		}
		return ((User) UserToolkit.getAdmin().get(0)).getUserid();
	}

	protected int getSearchMethod() {
		return Organization.ROLE_SEARCH_UP;
	}

	protected abstract String getRoleId();

}
