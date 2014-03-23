package com.sg.business.commons.ui.page.flow;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.commons.ui.flow.ProcessSettingPanel;
import com.sg.business.model.AbstractRoleAssignment;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.IRoleParameter;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.Role;
import com.sg.business.model.RoleDefinition;
import com.sg.business.model.RoleParameter;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public abstract class AbstractWorkdProcessPage extends AbstractProcessPage {

	@Override
	protected DataSet getRoleDataSet() {
		PrimaryObjectEditorInput input = getInput();
		WorkDefinition workd = (WorkDefinition) input.getData();
		if (workd != null) {
			List<PrimaryObject> rds = workd.getRoleDefinitions();
			return new DataSet(rds);
		}
		return null;
	}

	@Override
	protected List<DroolsProcessDefinition> getProcessDefinition() {
		PrimaryObjectEditorInput input = getInput();
		WorkDefinition workd = (WorkDefinition) input.getData();
		if (workd != null) {
			Organization org;
			if (workd.isProjectWork()) {
				ProjectTemplate pjt = workd.getProjectTemplate();
				org = pjt.getOrganization();
			} else {
				org = workd.getOrganization();
			}
			if (org != null) {
				return org.getDroolsProcessDefinitions();
			}
		}
		return null;
	}

	protected DataSet getActorDataSet(AbstractRoleDefinition roled) {
		// �����ɫ���岻Ϊ�գ�ȡ��ɫ�µ��û�
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		Role role = roled.getOrganizationRole();
		List<PrimaryObject> assignments;
		// ����roled�����ͽ����ж�
		if (roled instanceof ProjectRole) {
			// �������Ŀʱ��ʹ��ProjectRole.getAssignment();
			assignments = ((ProjectRole) roled).getAssignment();
		} else {
			// ����Ƕ�������ʱ��ʹ��TYPEΪTYPE_WORK_PROCESS��RoleParameter�����빤��ID������Աָ��
			IRoleParameter roleParameter;
			ObjectId work_id = (ObjectId) roled
					.getValue(RoleDefinition.F_WORK_ID);
			if (work_id != null) {
				Work work = ModelService.createModelObject(Work.class, work_id);
				 roleParameter = work.getAdapter(IRoleParameter.class);
			} else {
				roleParameter = new RoleParameter();
			}
			assignments = role.getAssignment(roleParameter);
		}
		for (int i = 0; i < assignments.size(); i++) {
			AbstractRoleAssignment ass = (AbstractRoleAssignment) assignments
					.get(i);
			String userid = ass.getUserid();
			User user = UserToolkit.getUserById(userid);
			if (!result.contains(user)) {
				result.add(user);
			}

		}
		return new DataSet(result);
	}

	@Override
	protected int getProcessSettingControl() {
		return ProcessSettingPanel.PROCESS_SELECTOR
				| ProcessSettingPanel.ROLE_SELECTOR
				| ProcessSettingPanel.ACTOR_SELECTOR;
	}

}
