package com.sg.business.management.editor.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class ProcessDefinitionPage implements IPageDelegator, IFormPart {

	private boolean dirty= false;
	private IManagedForm form;
	private ProjectTemplate projectTemplate;

	public ProcessDefinitionPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		parent.setBackgroundMode(SWT.INHERIT_DEFAULT);
		projectTemplate = (ProjectTemplate) input.getData();


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
		ProcessSettingPanel psp = new ProcessSettingPanel(tab,key,projectTemplate){

			@Override
			protected void setDirty(boolean b) {
				ProcessDefinitionPage.this.setDirty(b);
			}
		};
		return psp;
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
