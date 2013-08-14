package com.sg.business.management.editor.page;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class WorkDefinitionPage implements IPageDelegator, IFormPart {

	private boolean dirty= false;
	private IManagedForm form;
	private List<PrimaryObject> roleDefinitions;
	private WorkDefinition workDefinition;

	public WorkDefinitionPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		parent.setBackgroundMode(SWT.INHERIT_DEFAULT);
		parent.setLayout(new FillLayout());
		workDefinition = (WorkDefinition) input.getData();
		ProjectTemplate projectTemplate = workDefinition.getProjectTemplate();
		
		roleDefinitions = projectTemplate.getRoleDefinitions();

		CTabFolder tabFolder = new CTabFolder(parent, SWT.BOTTOM|SWT.FLAT);
		CTabItem cti1 = new CTabItem(tabFolder, SWT.NONE);
		cti1.setText("工作流程");
		Control control1 = createTab(tabFolder, WorkDefinition.F_WF_EXECUTE);
		cti1.setControl(control1);

		CTabItem cti2 = new CTabItem(tabFolder, SWT.NONE);
		cti2.setText("变更流程");
		Control control2 = createTab(tabFolder, WorkDefinition.F_WF_CHANGE);
		cti2.setControl(control2);

		return tabFolder;
	}

	private Control createTab(Composite tab, final String key) {
		ProcessSettingPanel psp = new ProcessSettingPanel(tab,key,workDefinition){
			@Override
			protected void setDirty(boolean b) {
				WorkDefinitionPage.this.setDirty(b);
			}
		};
		psp.setRoleDefinitions(roleDefinitions);
		psp.createContent();
		return psp;
	}


	private void setDirty(boolean b) {
		this.dirty = b;
		if(form!=null){
			form.dirtyStateChanged();
		}
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
		if(onSave){
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
