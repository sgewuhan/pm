package com.sg.business.project.test;

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.expressions.PropertyTester;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.RoleParameter;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;

public class ForceTest extends PropertyTester {

	public ForceTest() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver instanceof Work) {
			Work work = (Work) receiver;
			Project project = work.getProject();
			if (project != null) {
				if ("forceupdate".equals(property) && args != null
						&& args.length > 0) {
					if ("force".equals(args[0])) {
						IContext context = new CurrentAccountContext();
						String consignerId = context.getAccountInfo().getConsignerId();
						Organization org = project.getFunctionOrganization();
						Role role = org.getRole(Role.ROLE_PROJECT_ADMIN_ID,
								Organization.ROLE_NOT_SEARCH);
						// ʹ��TYPEΪTYPE_WORK��RoleParameter�����빤��ID������Աָ��
						HashMap<String, Object> parameters = new HashMap<String, Object>();
						parameters.put(RoleParameter.TYPE,
								RoleParameter.TYPE_WORK);
						parameters.put(RoleParameter.WORK_ID, work.get_id());
						parameters.put(RoleParameter.WORK, work);
						User charger = work.getCharger();
						if (charger != null) {
							parameters.put(RoleParameter.WORK_CHARGER, work
									.getCharger().getUserid());
						} else {
							parameters.put(RoleParameter.WORK_CHARGER, "");
						}
						parameters
								.put(RoleParameter.WORK_MILESTONE, work.isMilestone());
						parameters.put(RoleParameter.WORK_TYPE, work.getWorkType());
						List<PrimaryObject> assignmentList = role
								.getAssignment(parameters);
						if (assignmentList != null && assignmentList.size() > 0) {
							for (PrimaryObject po : assignmentList) {
								RoleAssignment roleAssignment = (RoleAssignment) po;
								String assignmentuserId = roleAssignment
										.getUserid();
								if (consignerId.equals(assignmentuserId)) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

}
