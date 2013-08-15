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

public abstract class AbstractWorkProcessPage implements IPageDelegator {

	public AbstractWorkProcessPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		
		
		WorkDefinition workDefinition = (WorkDefinition) input.getData();
		
		ProcessSettingPanel psp = new ProcessSettingPanel(parent,getWorkflowKey(),workDefinition);
		int type = workDefinition.getWorkDefinitionType();
		if(type == WorkDefinition.WORK_TYPE_PROJECT){
			ProjectTemplate projectTemplate = workDefinition.getProjectTemplate();
			List<PrimaryObject> roleDefinitions = projectTemplate.getRoleDefinitions();
			psp.setRoleDefinitions(roleDefinitions);
		}else if(type == WorkDefinition.WORK_TYPE_STANDLONE||type == WorkDefinition.WORK_TYPE_GENERIC){
			List<PrimaryObject> roles = workDefinition.getParticipateRoles();
			psp.setRoleDefinitions(roles);
		}
		psp.createContent();
		return psp;
	}

	protected abstract String getWorkflowKey() ;

	@Override
	public IFormPart getFormPart() {
		return null;
	}

}
