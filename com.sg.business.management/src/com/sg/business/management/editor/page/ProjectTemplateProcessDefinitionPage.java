package com.sg.business.management.editor.page;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.ui.flow.ProcessControlSetting;
import com.sg.business.commons.ui.flow.ProcessSettingPanel2;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class ProjectTemplateProcessDefinitionPage implements IPageDelegator,
		IFormPart {

	private boolean dirty;
	private IManagedForm form;
	private IProcessControl IProcessControl;
	private ProjectTemplate projectTemplate;

	public ProjectTemplateProcessDefinitionPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		parent.setBackgroundMode(SWT.INHERIT_DEFAULT);
		projectTemplate = (ProjectTemplate) input.getData();
		IProcessControl = (IProcessControl) projectTemplate
				.getAdapter(IProcessControl.class);

		// 项目计划提交流程
		CTabFolder tabFolder = new CTabFolder(parent, SWT.TOP);
		CTabItem cti1 = new CTabItem(tabFolder, SWT.NONE);
		cti1.setText("项目计划提交流程");
		// 为tabFolder添加显示组件
		Control control1 = createTab(tabFolder, ProjectTemplate.F_WF_COMMIT);
		cti1.setControl(control1);

		// 项目变更流程
		CTabItem cti2 = new CTabItem(tabFolder, SWT.NONE);
		cti2.setText("项目变更流程");
		// 为tabFolder添加显示组件
		Control control2 = createTab(tabFolder, ProjectTemplate.F_WF_CHANGE);
		cti2.setControl(control2);

		return tabFolder;
	}

	private Control createTab(Composite tab, final String key) {

		ProcessSettingPanel2 psp2 = new ProcessSettingPanel2(tab) {

			@Override
			protected AbstractRoleDefinition getRoleDefinition(
					NodeAssignment nodeAssignment) {
				if(nodeAssignment!=null){
					return IProcessControl.getProcessActionAssignment(key,
							nodeAssignment.getNodeActorParameter());
				}else{
					return null;
				}
			}

			@Override
			protected User getActor(NodeAssignment nodeAssignment) {
				String userid = IProcessControl.getProcessActionActor(key,
						nodeAssignment.getNodeActorParameter());
				return UserToolkit.getUserById(userid);
			}

		};

//		psp2.setHasActorSelector(true);
		psp2.setHasProcessSelector(true);
		psp2.setHasRoleSelector(true);

		// 返回模板所属组织下流程库中的所有组织，作为流程选择器的数据源
		List<DroolsProcessDefinition> processDefs = getDroolsProcessDefinitions();
		// 添加流程定义选择器
		psp2.setProcessDefinitionChoice(processDefs);

		boolean activate = isActivate(key);
		// 添加该流程是否启用的选择框
		psp2.setProcessActivated(activate);

		// 返回当前选中流程
		DroolsProcessDefinition processDef = getCurrentDroolsProcessDefinition(key);
		// 显示当前选中流程的信息
		psp2.setProcessDefinition(processDef);

		// 设置角色的选择器，项目模板中的角色定义
		psp2.setRoleNavigatorId("commons.generic.tableselector");

		// 设置角色的数据集
		List<PrimaryObject> rds = projectTemplate.getRoleDefinitions();
		psp2.setRoleDataSet(new DataSet(rds));

		psp2.createContent();

		// 添加监听
		psp2.addProcessSettingListener(new ProcessControlSetting(IProcessControl,key){
			@Override
			protected void event(int code) {
				setDirty(true);
			}
		});
		return psp2;
	}

	/**
	 * 返回当前流程定义
	 * 
	 * @param key
	 *            ,流程定义类型
	 * @return DroolsProcessDefinition
	 */
	private DroolsProcessDefinition getCurrentDroolsProcessDefinition(String key) {
		return IProcessControl.getProcessDefinition(key);
	}

	/**
	 * 判断当前流程是否启用
	 * 
	 * @param key
	 * @return boolean
	 */
	private boolean isActivate(String key) {
		return IProcessControl.isWorkflowActivate(key);
	}

	/**
	 * 返回模板所属组织下流程库中所有流程定义
	 * 
	 * @return List
	 */
	private List<DroolsProcessDefinition> getDroolsProcessDefinitions() {
		// 获取模板所属的组织
		Organization org = projectTemplate.getOrganization();
		return org.getDroolsProcessDefinitions();
	}

	private void setDirty(boolean b) {
		this.dirty = b;
		form.dirtyStateChanged();
	}

	@Override
	public IFormPart getFormPart() {
		return this;
	}

	@Override
	public void initialize(IManagedForm form) {
		this.form = form;
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void commit(boolean onSave) {
		if (onSave) {
			dirty = false;
			form.dirtyStateChanged();
		}
	}

	@Override
	public boolean setFormInput(Object input) {
		return false;
	}

	@Override
	public void setFocus() {

	}

	@Override
	public boolean isStale() {
		return false;
	}

	@Override
	public void refresh() {

	}

}
