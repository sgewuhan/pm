package com.sg.business.commons.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.RoleParameter;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public abstract class AbstractRoleAssignmentDataSetFactory extends
		MasterDetailDataSetFactory {

	protected IContext context;
	protected User user;

	public AbstractRoleAssignmentDataSetFactory() {
		super(IModelConstants.DB, IModelConstants.C_USER);
		context = new CurrentAccountContext();
		String userId = context.getAccountInfo().getUserId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	@Override
	public DataSet getDataSet() {
		if (master != null) {
			if (master instanceof TaskForm) {
				TaskForm taskForm = (TaskForm) master;
				List<PrimaryObject> result = new ArrayList<PrimaryObject>();
				try {
					Organization org = user.getOrganization();
					Work work = taskForm.getWork();
					Role role = org.getRole(getRoleNumber(), getSelectType());
					// 使用TYPE为TYPE_WORK_PROCESS的RoleParameter，传入工作ID进行人员指派
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put(RoleParameter.TYPE,
							RoleParameter.TYPE_WORK_PROCESS);
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
					result.addAll(assignment);
					return new DataSet(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.getDataSet();
	}

	protected abstract int getSelectType();

	protected abstract String getRoleNumber();

}
