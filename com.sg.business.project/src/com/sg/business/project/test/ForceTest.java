package com.sg.business.project.test;

import java.util.List;

import org.eclipse.core.expressions.PropertyTester;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IRoleParameter;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
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
						String consignerId = context.getAccountInfo()
								.getConsignerId();
						Organization org = project.getFunctionOrganization();
						Role role = org.getRole(Role.ROLE_PROJECT_ADMIN_ID,
								Organization.ROLE_NOT_SEARCH);
						// 使用TYPE为TYPE_WORK的RoleParameter，传入工作ID进行人员指派
						IRoleParameter roleParameter = work
								.getAdapter(IRoleParameter.class);
						List<PrimaryObject> assignmentList = role
								.getAssignment(roleParameter);
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
