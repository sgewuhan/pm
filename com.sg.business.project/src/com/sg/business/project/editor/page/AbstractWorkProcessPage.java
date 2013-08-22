package com.sg.business.project.editor.page;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public abstract class AbstractWorkProcessPage implements IPageDelegator,
		IFormPart {

	private boolean dirty;
	private IManagedForm form;

	public AbstractWorkProcessPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {

		parent.setBackgroundMode(SWT.INHERIT_DEFAULT);

		Work workDefinition = (Work) input.getData();

		ProcessSettingPanel psp = new ProcessSettingPanel(parent,
				getWorkflowKey(), workDefinition) {
			@Override
			protected void setDirty(boolean b) {
				AbstractWorkProcessPage.this.setDirty(b);
			}
		};
		Project projectTemplate = workDefinition.getProject();
		List<PrimaryObject> roleDefinitions = projectTemplate
				.getRoleDefinitions();
		psp.setRoleDefinitions(roleDefinitions);
		psp.createContent();
		return psp;
	}

	protected abstract String getWorkflowKey();

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

	private void setDirty(boolean b) {
		this.dirty = b;
		if (form != null) {
			form.dirtyStateChanged();
		}
	}

	@Override
	public void commit(boolean onSave) {
		setDirty(false);
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
