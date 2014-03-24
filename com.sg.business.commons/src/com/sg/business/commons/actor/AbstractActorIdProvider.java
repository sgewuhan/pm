package com.sg.business.commons.actor;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.service.actor.IActorIdProvider;
import com.sg.business.model.IRoleParameter;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;

public abstract class AbstractActorIdProvider implements IActorIdProvider {

	@Override
	public String getActorId(Object[] input) {
		Work work = (Work) input[0];
		Organization org = getOrganization(input, work);
		Role role = org.getRole(getRoleNumber(), getSelectType());
		if (role != null) {
			// 使用TYPE为TYPE_WORK_PROCESS的RoleParameter，传入工作ID进行人员指派
			IRoleParameter roleParameter = work
					.getAdapter(IRoleParameter.class);
			List<PrimaryObject> assignment = role.getAssignment(roleParameter);
			if (assignment != null && assignment.size() > 0) {
				return ((RoleAssignment) assignment.get(0)).getUserid();
			}
		}
		return ((User) UserToolkit.getAdmin().get(0)).getUserid();
	}

	protected Organization getOrganization(Object[] input, Work work) {
		User charger = work.getCharger();
		Organization org = charger.getOrganization();
		return org;
	}

	protected abstract int getSelectType();

	protected abstract String getRoleNumber();

}
