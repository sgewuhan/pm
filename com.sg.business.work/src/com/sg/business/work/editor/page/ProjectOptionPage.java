package com.sg.business.work.editor.page;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.widgets.IWidgetGraphicsAdapter;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.Section;

import com.mongodb.BasicDBList;
import com.sg.business.model.Project;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

@SuppressWarnings("restriction")
public class ProjectOptionPage implements IPageDelegator{

	private Project project;
	private Composite content;

	public ProjectOptionPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		content = new Composite(parent, SWT.NONE);
		content.setLayout(new GridLayout());
		project = (Project) input.getData();
		createOptionContent();
		return content;
	}

	private Composite createOptionSetting(Composite parent, List<?> options,
			final String title, final String fieldName) {
		Section section = new Section(parent, Section.CLIENT_INDENT
				| Section.SHORT_TITLE_BAR | Section.TWISTIE | Section.EXPANDED);

		Composite separator = new Composite(section, SWT.NONE);
		Display display = separator.getDisplay();
		Object adapter = separator.getAdapter(IWidgetGraphicsAdapter.class);
		IWidgetGraphicsAdapter gfxAdapter = (IWidgetGraphicsAdapter) adapter;
		int[] percents = new int[] { 0, 50, 100 };
		Color[] gradientColors = new Color[] {
				Widgets.getColor(display, 200, 200, 200),
				Widgets.getColor(display, 245, 245, 245),
				Widgets.getColor(display, 250, 250, 250) };

		gfxAdapter.setBackgroundGradient(gradientColors, percents, false);

		section.setSeparatorControl(separator);

		section.setText(title);
		Composite sectionClient = new Composite(section, SWT.NONE);
		GridLayout glayout = new GridLayout(1, false);
		sectionClient.setLayout(glayout);

		BasicDBList optionsValue = (BasicDBList) project.getValue(fieldName);

		for (int i = 0; i < options.size(); i++) {
			final Button settingButton = new Button(sectionClient, SWT.CHECK);
			final String optionName = (String) options.get(i);
			settingButton.setData("data", optionName);
			settingButton.setText(optionName);
			settingButton.setEnabled(false);
			settingButton.setSelection(optionsValue != null
					&& optionsValue.contains(optionName));
		}
		section.setClient(sectionClient);
		return section;
	}




	private void createOptionContent() {
		Control[] children = content.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}

		
		if(project==null){
			return;
		}
		List<?> standardOptions = (List<?>) project
				.getValue(Project.F_STANDARD_SET_OPTION);
		createOptionSetting(content, standardOptions, "标准",
				Project.F_STANDARD_SET_OPTION);

		List<?> productTypeOptions = (List<?>) project
				.getValue(Project.F_PRODUCT_TYPE_OPTION);
		createOptionSetting(content, productTypeOptions, "产品类型",
				Project.F_PRODUCT_TYPE_OPTION);

		List<?> projectTypeOptions = (List<?>) project
				.getValue(Project.F_PROJECT_TYPE_OPTION);
		createOptionSetting(content, projectTypeOptions, "项目类型",
				Project.F_PROJECT_TYPE_OPTION);

		content.layout();		
	}

	@Override
	public IFormPart getFormPart() {
		return null;
	}



}
