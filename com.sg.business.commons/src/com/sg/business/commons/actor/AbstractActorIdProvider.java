package com.sg.business.commons.actor;

import java.util.HashMap;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.service.actor.IActorIdProvider;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.RoleParameter;
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
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(RoleParameter.TYPE, RoleParameter.TYPE_WORK_PROCESS);
			parameters.put(RoleParameter.WORK_ID, work.get_id());
			parameters.put(RoleParameter.WORK, work);
			User charger = work.getCharger();
			if (charger != null) {
				parameters.put(RoleParameter.WORK_CHARGER, work.getCharger()
						.getUserid());
			} else {
				parameters.put(RoleParameter.WORK_CHARGER, "");
			}
			parameters.put(RoleParameter.WORK_MILESTONE, work.isMilestone());
			parameters.put(RoleParameter.WORK_TYPE, work.getWorkType());
			List<PrimaryObject> assignment = role.getAssignment(parameters);
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
