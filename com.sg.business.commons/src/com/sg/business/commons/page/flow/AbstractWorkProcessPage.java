package com.sg.business.commons.page.flow;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.commons.ui.flow.ProcessSettingPanel2;
import com.sg.business.model.AbstractRoleAssignment;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.Role;
import com.sg.business.model.RoleDefinition;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public abstract class AbstractWorkProcessPage extends AbstractProcessPage {

	protected Work getWork() {
		PrimaryObjectEditorInput input = getInput();
		Work work = (Work) input.getData();
		return work;
	}
	
	@Override
	protected String getActorNavigatorId() {
		//����Ƿ���Ŀ����
		Work work = getWork();
		Project project = work.getProject();
		if(project==null){
			//û�й�����Ŀ�Ķ�������
			ProcessSettingPanel2 psp = getProcessSettingPanel();
			AbstractRoleDefinition role = psp.getSelectedRole();
			if(role == null){
				return "organization.user.selector";
			}
		}
		
		return super.getActorNavigatorId();
	}
	

	@Override
	public ProcessSettingPanel2 createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		ProcessSettingPanel2 psp = super.createPageContent(parent, input, conf);
		// ���ƽ�ɫ�޶��ڹ����в��ɱ��޸�
		psp.setRoleSelectEnable(false);
		return psp;
	}

	@Override
	protected IProcessControl getIProcessControl() {
		Work work = getWork();
		return (IProcessControl) work.getAdapter(IProcessControl.class);
	}

	@Override
	protected DataSet getRoleDataSet() {
		Work work = getWork();

		Project project = work.getProject();
		if (project != null) {
			List<PrimaryObject> rds = project.getProjectRole();
			return new DataSet(rds);
		}
		return null;
	}

	@Override
	protected List<DroolsProcessDefinition> getProcessDefinition() {
		Work work = getWork();
		// �����������Ŀ�Ĺ����������Ŀ��������Ŀ����ְ����֯
		Project project = work.getProject();
		if (project != null) {
			Organization org = project.getFunctionOrganization();
			return org.getDroolsProcessDefinitions();
		}
		return null;
	}

	@Override
	protected DataSet getActorDataSet(AbstractRoleDefinition roled) {
		Work work = getWork();

		// �����ɫ���岻Ϊ�գ�ȡ��ɫ�µ��û�
		List<PrimaryObject> assignments = null;
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		if (roled instanceof ProjectRole) {
			ProjectRole projectRole = (ProjectRole) roled;
			assignments = projectRole.getAssignment();
		} else if (roled instanceof RoleDefinition) {
			RoleDefinition roledef = (RoleDefinition) roled;
			Role role = roledef.getOrganizationRole();
			if (role != null) {
				assignments = role.getAssignment();
			}
		}

		// ָ�ɲ�Ϊ��
		if (assignments != null) {
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

		// ���޷�ȡ��ָ��ʱ������Ŀ�л����Ա
		Project project = work.getProject();
		if (project != null) {
			List<?> useridList = project.getParticipatesIdList();
			for (int i = 0; i < useridList.size(); i++) {
				String userid = (String) useridList.get(i);
				User user = UserToolkit.getUserById(userid);
				if (!result.contains(user)) {
					result.add(user);
				}
			}
			return new DataSet(result);
		}else{
			//û�ж�����Ŀ�Ĺ���������������
			return null;
		}
	}

	@Override
	protected int getProcessSettingControl() {
		return ProcessSettingPanel2.PROCESS_SELECTOR
				| ProcessSettingPanel2.ROLE_SELECTOR;
	}

}