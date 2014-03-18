package com.tmt.jszx.option;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.taskforms.IRoleConstance;
import com.sg.widgets.commons.options.IFieldOptionProvider;
import com.sg.widgets.registry.config.Option;

public class ChiefEngineerOption implements IFieldOptionProvider {

	public ChiefEngineerOption() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Option getOption(Object input, Object data, String key, Object value) {

		Object dept = ((PrimaryObject) data).getValue("dept"); //$NON-NLS-1$
		if (dept instanceof ObjectId) {
			Organization org = ModelService.createModelObject(
					Organization.class, (ObjectId) dept);
			Role role = org.getRole(IRoleConstance.ROLE_CHIEF_ENGINEER_ID,
					Organization.ROLE_SEARCH_UP);
			if (role != null) {
				//TODO 使用TYPE为TYPE_WORK_PROCESS的RoleParameter，传入工作ID进行人员指派
				List<PrimaryObject> assignment = role.getAssignment();
				if (assignment != null && assignment.size() > 0) {
					List<Option> children = new ArrayList<Option>();
					for (PrimaryObject po : assignment) {
						String userId = ((RoleAssignment) po).getUserid();
						User user = UserToolkit.getUserById(userId);
						children.add(new Option(user.getUserid(), user
								.getLabel(), user.getUserid(), null));
					}
					return new Option(
							"", "", "", children.toArray(new Option[0])); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

				}
			}
		}
		return null;
	}

}
