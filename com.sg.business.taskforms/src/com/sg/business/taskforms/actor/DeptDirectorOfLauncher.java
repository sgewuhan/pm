package com.sg.business.taskforms.actor;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.service.actor.IActorIdProvider;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class DeptDirectorOfLauncher implements IActorIdProvider {

	public DeptDirectorOfLauncher() {
	}

	@Override
	public String getActorId(Object[] input) {
		Work work = (Work) input[0];
		String chargerId = work.getChargerId();
		User loginUser = UserToolkit.getUserById(chargerId);
		Organization org = loginUser.getOrganization();
		while (org != null) {
			Role role = org.getRole("Director");
			if (role != null) {
				List<PrimaryObject> assignment = role.getAssignment();
				if (assignment != null && assignment.size() > 0) {
					return ((RoleAssignment) assignment.get(0)).getUserid();
				}
			}
			org = (Organization) org.getParentOrganization();
		}
		return ((User) UserToolkit.getAdmin().get(0)).getUserid();
	}

}
