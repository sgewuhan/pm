package com.sg.business.management.editor.page.process;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.commons.page.AbstractProcessSettingPage;
import com.sg.business.commons.ui.flow.ProcessSettingPanel2;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public abstract class AbstractProjectTemplateProcessSettingPage extends
		AbstractProcessSettingPage {


	@Override
	protected DataSet getRoleDataSet() {
		PrimaryObjectEditorInput input = getInput();
		ProjectTemplate projectTemplate = (ProjectTemplate) input.getData();
		if (projectTemplate != null) {
			List<PrimaryObject> rds = projectTemplate.getRoleDefinitions();
			return new DataSet(rds);
		}
		return null;
	}

	@Override
	protected List<DroolsProcessDefinition> getProcessDefinition() {
		PrimaryObjectEditorInput input = getInput();
		ProjectTemplate projectTemplate = (ProjectTemplate) input.getData();
		if (projectTemplate != null) {
			Organization org = projectTemplate.getOrganization();
			return org.getDroolsProcessDefinitions();
		}
		return null;
	}

	protected DataSet getActorDataSet(AbstractRoleDefinition roled) {
		return null;//项目模板无需设定到具体人
	}

	@Override
	protected int getProcessSettingControl() {
		return ProcessSettingPanel2.PROCESS_SELECTOR
				| ProcessSettingPanel2.ROLE_SELECTOR;
	}

}
