package com.sg.business.commons.deprecatedclass;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.ui.flow.ProcessControlSetting;
import com.sg.business.commons.ui.flow.ProcessSettingPanel2;
import com.sg.business.model.AbstractRoleAssignment;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

@Deprecated
public class WorkExecuteProcessAssignmentPage extends AbstractFormPageDelegator {

	private Work work;
	private boolean editable;
	private ProcessSettingPanel2 psp2;

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		work = (Work) input.getData();
		editable = input.isEditable();
		final IProcessControl IProcessControl = (IProcessControl) work
				.getAdapter(IProcessControl.class);
		psp2 = new ProcessSettingPanel2(parent,
				ProcessSettingPanel2.ACTOR_SELECTOR
						| ProcessSettingPanel2.ROLE_SELECTOR) {

			@Override
			protected AbstractRoleDefinition getRoleDefinition(
					NodeAssignment nodeAssignment) {
				if (nodeAssignment != null) {
					return IProcessControl.getProcessActionAssignment(
							Work.F_WF_EXECUTE,
							nodeAssignment.getNodeActorParameter());
				} else {
					return null;
				}
			}

			@Override
			protected User getActor(NodeAssignment nodeAssignment) {
				String userid = IProcessControl.getProcessActionActor(
						Work.F_WF_EXECUTE,
						nodeAssignment.getNodeActorParameter());
				return UserToolkit.getUserById(userid);
			}

			@Override
			public DataSet getActorDataSet() {
				AbstractRoleDefinition roled = getSelectedRole();
				return WorkExecuteProcessAssignmentPage.this
						.getActorDataSet(roled);
			}

			@Override
			protected String getRoleNavigatorId() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getActorNavigatorId(AbstractRoleDefinition roled) {
				// TODO Auto-generated method stub
				return null;
			}

		};

		// ���ص�ǰѡ������
		DroolsProcessDefinition processDef = IProcessControl
				.getProcessDefinition(Work.F_WF_EXECUTE);
		// ��ʾ��ǰѡ�����̵���Ϣ
		psp2.setProcessDefinition(processDef);

		// // ���ý�ɫ��ѡ��������Ŀģ���еĽ�ɫ����
		// psp2.setRoleNavigatorId("commons.generic.tableselector");
		//
		// // �����û���ѡ����
		// psp2.setActorNavigatorId("commons.generic.tableselector");

		// ���ý�ɫ�����ݼ�
		Project project = work.getProject();
		if (project != null) {
			List<PrimaryObject> rds = project.getProjectRole();
			psp2.setRoleDataSet(new DataSet(rds));
		}

		psp2.createContent();

		// ��Ӽ���
		psp2.addProcessSettingListener(new ProcessControlSetting(
				IProcessControl, Work.F_WF_EXECUTE) {
			@Override
			protected void event(int code) {
				setDirty(true);
			}
		});

		psp2.setEditable(editable);
		return psp2;
	}

	protected DataSet getActorDataSet(AbstractRoleDefinition roled) {
		// �����ɫ���岻Ϊ�գ�ȡ��ɫ�µ��û�
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
			Project project = work.getProject();
			List<?> useridList = project.getParticipatesIdList();
			if (useridList != null) {
				for (int i = 0; i < useridList.size(); i++) {
					String userid = (String) useridList.get(i);
					User user = UserToolkit.getUserById(userid);
					if (!result.contains(user)) {
						result.add(user);
					}
				}
			}
		}

		// �����ɫ����Ϊ�գ�ȡ��Ŀ�Ĳ�����
		return new DataSet(result);
	}

	@Override
	public void commit(boolean onSave) {
		setDirty(false);
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		super.refresh();
	}

}
