package com.sg.business.management.editor.page;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class WorkChangeProcessPage implements IPageDelegator {

	public WorkChangeProcessPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		
		WorkDefinition workDefinition = (WorkDefinition) input.getData();
		ProjectTemplate projectTemplate = workDefinition.getProjectTemplate();
		
		List<PrimaryObject> roleDefinitions = projectTemplate.getRoleDefinitions();

		
		ProcessSettingPanel psp = new ProcessSettingPanel(parent,WorkDefinition.F_WF_CHANGE,workDefinition){
			@Override
			protected void setDirty(boolean b) {
			}
		};
		psp.setRoleDefinitions(roleDefinitions);
		psp.createContent();
		
		return psp;
	}

	@Override
	public IFormPart getFormPart() {
		return null;
	}

}
