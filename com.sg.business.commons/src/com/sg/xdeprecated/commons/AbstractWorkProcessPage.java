package com.sg.xdeprecated.commons;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

@Deprecated
public abstract class AbstractWorkProcessPage implements IPageDelegator,
		IFormPart {

	private boolean dirty;
	private IManagedForm form;
	private ProcessSettingPanel psp;

	public AbstractWorkProcessPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {

		parent.setBackgroundMode(SWT.INHERIT_DEFAULT);

		PrimaryObject po = input.getData();

		psp = new ProcessSettingPanel(parent,
				getWorkflowKey(), po, input.isEditable()) {
			@Override
			protected void setDirty(boolean b) {
				AbstractWorkProcessPage.this.setDirty(b);
			}

		};
		psp.setRoleDefinitions(getRoleDefinitions(po));
		psp.createContent();
		return psp;
	}


	/**
	 * 获取角色定义
	 * 
	 * @param po
	 * @return
	 */
	protected List<PrimaryObject> getRoleDefinitions(PrimaryObject po) {
		Project project = null;
		if (po instanceof IProjectRelative) {
			project = ((IProjectRelative) po).getProject();
		} else if (po instanceof Project) {
			project = (Project) po;
		}

		if (project != null) {
			return project.getProjectRole();
		}

		return null;
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

	public void setDirty(boolean b) {
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
