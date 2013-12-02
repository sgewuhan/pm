package com.sg.business.taskforms.actor;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.service.actor.IActorIdProvider;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;

public class ProjectAdminOfProject implements IActorIdProvider {

	public ProjectAdminOfProject() {
	}

	@Override
	public String getActorId(Object[] input) {
		Work work = (Work) input[0];
		Project project = work.getProject();
		Organization org = project.getFunctionOrganization();
		Role role = org.getRole(Role.ROLE_PROJECT_ADMIN_ID, Organization.ROLE_NOT_SEARCH);
		if (role != null) {
			List<PrimaryObject> assignment = role.getAssignment();
			if (assignment != null && assignment.size() > 0) {
				return ((RoleAssignment) assignment.get(0)).getUserid();
			}
		}
		return ((User) UserToolkit.getAdmin().get(0)).getUserid();
	}

}
