package com.sg.business.project.editor.page;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.commons.page.AbstractProcessSettingPage;
import com.sg.business.commons.ui.flow.ProcessSettingPanel2;
import com.sg.business.model.AbstractRoleAssignment;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

/**
 * 项目提交时，如果指定了流程，将自动创建独立工作用于项目提交<br/>
 * 该独立工作的角色设置是组织的角色，而并非项目角色定义。所以，需要覆盖获取角色定义部分的代码
 * 
 * @author zhonghua
 * 
 */
public abstract class AbstractProjectProcessSettingPage extends
		AbstractProcessSettingPage {

	@Override
	protected IProcessControl getIProcessControl() {
		PrimaryObjectEditorInput input = getInput();
		Project project = (Project) input.getData();
		return (IProcessControl) project.getAdapter(IProcessControl.class);
	}

	@Override
	protected DataSet getRoleDataSet() {
		PrimaryObjectEditorInput input = getInput();
		Project project = (Project) input.getData();
		if (project != null) {
			List<PrimaryObject> rds = project.getProjectRole();
			return new DataSet(rds);
		}
		return null;
	}

	@Override
	protected List<DroolsProcessDefinition> getProcessDefinition() {
		PrimaryObjectEditorInput input = getInput();
		Project project = (Project) input.getData();
		if (project != null) {
			Organization org = project.getFunctionOrganization();
			return org.getDroolsProcessDefinitions();
		}
		return null;
	}

	@Override
	protected DataSet getActorDataSet(AbstractRoleDefinition roled) {
		// 如果角色定义不为空，取角色下的用户
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		if (roled instanceof ProjectRole) {
			ProjectRole projectRole = (ProjectRole) roled;
			List<PrimaryObject> assignments = projectRole.getAssignment();
			for (int i = 0; i < assignments.size(); i++) {
				AbstractRoleAssignment ass = (AbstractRoleAssignment) assignments
						.get(i);
				String userid = ass.getUserid();
				User user = UserToolkit.getUserById(userid);
				if (!result.contains(user)) {
					result.add(user);
				}
			}

		} else {
			PrimaryObjectEditorInput input = getInput();
			Project project = (Project) input.getData();
			List<?> useridList = project.getParticipatesIdList();
			for (int i = 0; i < useridList.size(); i++) {
				String userid = (String) useridList.get(i);
				User user = UserToolkit.getUserById(userid);
				if (!result.contains(user)) {
					result.add(user);
				}
			}
		}

		// 如果角色定义为空，取项目的参与者
		return new DataSet(result);
	}

	@Override
	protected int getProcessSettingControl() {
		return ProcessSettingPanel2.PROCESS_SELECTOR
				| ProcessSettingPanel2.ROLE_SELECTOR
				| ProcessSettingPanel2.ACTOR_SELECTOR;
	}

}
