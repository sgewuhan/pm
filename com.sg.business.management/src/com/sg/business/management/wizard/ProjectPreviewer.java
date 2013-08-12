package com.sg.business.management.wizard;

import org.eclipse.jface.wizard.Wizard;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.sg.business.model.AbstractOptionFilterable;
import com.sg.business.model.ProjectTemplate;

public class ProjectPreviewer extends Wizard {

	private OptionSettingPage optionSettingPage;
	private PreviewPage previewPage;
	private ProjectTemplate projectTemplate;
	private BasicDBList filters = new BasicDBList();

	public ProjectPreviewer(ProjectTemplate projectTemplate) {
		setWindowTitle("项目工作和交付物预览");
		setProjectTemplate(projectTemplate);
	}

	@Override
	public void addPages() {
		optionSettingPage = new OptionSettingPage();
		addPage(optionSettingPage);
		previewPage = new PreviewPage();
		addPage(previewPage);
		
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	public ProjectTemplate getProjectTemplate() {
		return projectTemplate;
	}

	public void setProjectTemplate(ProjectTemplate projectTemplate) {
		this.projectTemplate = projectTemplate;
	}

	public void setFilterCondition(String optionSet, String option,
			boolean selection) {
		BasicDBObject filter = new BasicDBObject().append(
				AbstractOptionFilterable.SF_OPTIONSET, optionSet).append(
				AbstractOptionFilterable.SF_OPTION, option);
		if (selection) {
			filters.add(filter);
		} else {
			filters.remove(filter);
		}
		
		previewPage.setFilters(filters);
	}

}
