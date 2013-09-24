package com.sg.business.work.page;

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
import com.sg.business.model.IProcessControlable;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public abstract class AbstractWorkProcessSettingPage extends AbstractFormPageDelegator {

	private Work work;
	private boolean editable;
	private ProcessSettingPanel2 psp2;

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		work = (Work) input.getData();
		editable = input.isEditable();
		final IProcessControlable processControl = (IProcessControlable) work
				.getAdapter(IProcessControlable.class);
		psp2 = new ProcessSettingPanel2(parent) {

			@Override
			protected AbstractRoleDefinition getRoleDefinition(
					NodeAssignment nodeAssignment) {
				if (nodeAssignment != null) {
					return processControl.getProcessActionAssignment(
							getProcessKey(),
							nodeAssignment.getNodeActorParameter());
				} else {
					return null;
				}
			}

			@Override
			protected User getActor(NodeAssignment nodeAssignment) {
				String userid = processControl.getProcessActionActor(
						getProcessKey(),
						nodeAssignment.getNodeActorParameter());
				return UserToolkit.getUserById(userid);
			}

			@Override
			public DataSet getActorDataSet() {
				AbstractRoleDefinition roled = getSelectedRole();
				return AbstractWorkProcessSettingPage.this
						.getActorDataSet(roled);
			}

		};

		psp2.setHasActorSelector(false);
		psp2.setHasProcessSelector(true);
		psp2.setHasRoleSelector(true);

		
		List<DroolsProcessDefinition> processDefs = getProcessDefinition();
		psp2.setProcessDefinitionChoice(processDefs);
		// 返回当前选中流程
		DroolsProcessDefinition processDef = processControl
				.getProcessDefinition(getProcessKey());
		// 显示当前选中流程的信息
		psp2.setProcessDefinition(processDef);

		// 设置角色的选择器，项目模板中的角色定义
		psp2.setRoleNavigatorId("commons.generic.tableselector");

		// 设置用户的选择器
		psp2.setActorNavigatorId("commons.generic.tableselector");

		// 设置角色的数据集
		Project project = work.getProject();
		if (project != null) {
			List<PrimaryObject> rds = project.getProjectRole();
			psp2.setRoleDataSet(new DataSet(rds));
		}

		psp2.createContent();

		// 添加监听
		psp2.addProcessSettingListener(new ProcessControlSetting(
				processControl, getProcessKey()) {
			@Override
			protected void event(int code) {
				setDirty(true);
			}
		});

		psp2.setEditable(editable);
		return psp2;
	}

	private List<DroolsProcessDefinition> getProcessDefinition() {
		//如果工作是项目的工作，获得项目所属的项目管理职能组织
		Project project = work.getProject();
		if(project!=null){
			Organization org = project.getFunctionOrganization();
			return org.getDroolsProcessDefinitions();
		}
		return null;
	}

	protected abstract String getProcessKey();

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
			Project project = work.getProject();
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
	public void commit(boolean onSave) {
		setDirty(false);
	}

	@Override
	public void setFocus() {
	}

}
