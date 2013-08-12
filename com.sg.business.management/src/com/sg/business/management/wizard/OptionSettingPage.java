package com.sg.business.management.wizard;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.widgets.IWidgetGraphicsAdapter;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Section;

import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.Widgets;

@SuppressWarnings("restriction")
public class OptionSettingPage extends WizardPage {

	protected OptionSettingPage() {
		super("选项设置");
		setDescription("请选择项目的选项条件，设置完成后点击下一步预览工作分解结构和交付物");
	}

	@Override
	public void createControl(Composite parent) {

		Composite content = new Composite(parent,SWT.NONE);
		content.setLayout(new GridLayout());

		ProjectPreviewer wiz = (ProjectPreviewer) getWizard();
		ProjectTemplate template = wiz.getProjectTemplate();
		List<?> standardOptions = (List<?>) template
				.getValue(ProjectTemplate.F_STANDARD_OPTION_SET);
		createOptionSetting(content, standardOptions, "标准");
		
		List<?> productTypeOptions = (List<?>) template
				.getValue(ProjectTemplate.F_PRODUCTTYPE_OPTION_SET);
		createOptionSetting(content, productTypeOptions, "产品类型");

		List<?> projectTypeOptions = (List<?>) template
				.getValue(ProjectTemplate.F_PROJECTTYPE_OPTION_SET);
		createOptionSetting(content, projectTypeOptions, "项目类型");

		setControl(content);
		setPageComplete(true);
	}

	@Override
	public boolean canFlipToNextPage() {
		return true;
	}

	private Composite createOptionSetting(Composite parent, List<?> standardOptions,
			final String title) {
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

		for (int i = 0; i < standardOptions.size(); i++) {
			final Button settingButton = new Button(sectionClient, SWT.CHECK);
			final String optionName = (String) standardOptions.get(i);
			settingButton.setData("data", optionName);
			settingButton.setText(optionName);
			settingButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					addFilterCondition(title,optionName,settingButton.getSelection());
				}
			});
		}

		section.setClient(sectionClient);
		return section;
	}

	protected void addFilterCondition(String optionSet, String option,
			boolean selection) {
		ProjectPreviewer wiz = (ProjectPreviewer) getWizard();
		wiz.setFilterCondition(optionSet, option,selection);
	}

}
