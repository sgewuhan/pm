package com.sg.business.management.editor.page;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.commons.ui.flow.ProcessSettingPanel2;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class ProjectTemplateProcessDefinitionPage implements IPageDelegator,
		IFormPart {

	private boolean dirty;
	private IManagedForm form;
	private ProjectTemplate projectTemplate;
	private List<PrimaryObject> roleDefinitions;

	public ProjectTemplateProcessDefinitionPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		parent.setBackgroundMode(SWT.INHERIT_DEFAULT);
		projectTemplate = (ProjectTemplate) input.getData();
		roleDefinitions = projectTemplate.getRoleDefinitions();

		CTabFolder tabFolder = new CTabFolder(parent, SWT.TOP);
		CTabItem cti1 = new CTabItem(tabFolder, SWT.NONE);
		cti1.setText("项目计划提交流程");
		Control control1 = createTab(tabFolder, ProjectTemplate.F_WF_COMMIT);
		cti1.setControl(control1);

		CTabItem cti2 = new CTabItem(tabFolder, SWT.NONE);
		cti2.setText("项目变更流程");
		Control control2 = createTab(tabFolder, ProjectTemplate.F_WF_CHANGE);
		cti2.setControl(control2);

		return tabFolder;
	}

	private Control createTab(Composite tab, final String key) {

		ProcessSettingPanel2 psp2 = new ProcessSettingPanel2(tab);
		psp2.setHasActorSelector(true);
		psp2.setHasProcessSelector(true);
		psp2.setHasRoleSelector(true);
		psp2.createContent();

		List<DroolsProcessDefinition> processDefs = getDroolsProcessDefinitions();

		psp2.setProcessDefinitionChoice(processDefs);

		boolean activate = isActivate(key);
		psp2.setProcessActivated(activate);

		DroolsProcessDefinition processDef = getCurrentDroolsProcessDefinition(key);
		psp2.setProcessDefinition(processDef);

		return psp2;
	}

	private DroolsProcessDefinition getCurrentDroolsProcessDefinition(String key) {
		return projectTemplate.getProcessDefinition(key);
	}

	private boolean isActivate(String key) {
		return projectTemplate.isWorkflowActivate(key);
	}

	private List<DroolsProcessDefinition> getDroolsProcessDefinitions() {
		//获取模板所属的组织
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
