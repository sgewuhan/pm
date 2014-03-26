package com.sg.business.management.editor.page;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.IEditorPageLayoutProvider;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;
@Deprecated
public abstract class AbstractWorkProcessPage implements IPageDelegator,
		IFormPart {

	private boolean dirty;
	private IManagedForm form;

	public AbstractWorkProcessPage() {
	}
	@Override
	public IEditorPageLayoutProvider getPageLayout() {
		return null;
	}
	

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {

		parent.setBackgroundMode(SWT.INHERIT_DEFAULT);

		WorkDefinition workDefinition = (WorkDefinition) input.getData();

		ProcessSettingPanel psp = new ProcessSettingPanel(parent,
				getWorkflowKey(), workDefinition){
			@Override
			protected void setDirty(boolean b) {
				AbstractWorkProcessPage.this.setDirty(b);
			}
		};
		int type = workDefinition.getWorkDefinitionType();
		if (type == WorkDefinition.WORK_TYPE_PROJECT) {
			ProjectTemplate projectTemplate = workDefinition
					.getProjectTemplate();
			List<PrimaryObject> roleDefinitions = projectTemplate
					.getRoleDefinitions();
			psp.setRoleDefinitions(roleDefinitions);
		} else if (type == WorkDefinition.WORK_TYPE_STANDLONE
				|| type == WorkDefinition.WORK_TYPE_GENERIC) {
			List<PrimaryObject> roles = workDefinition.getParticipateRoles();
			psp.setRoleDefinitions(roles);
		}
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
	
	@Override
	public boolean createBody() {
		return false;
	}

}
