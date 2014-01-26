package com.tmt.tb.editor.page;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.widgets.IWidgetGraphicsAdapter;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.Section;

import com.mobnut.db.model.IPrimaryObjectValueChangeListener;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBList;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.TaskForm;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.IValidable;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;
import com.tmt.tb.nls.Messages;

@SuppressWarnings("restriction")
public class StandloneWorkPageOfTB implements IPageDelegator, IFormPart,
		IPrimaryObjectValueChangeListener, IValidable {

	private boolean dirty;
	@SuppressWarnings("unused")
	private IManagedForm form;
	private TaskForm taskForm;
	private Composite content;
	private boolean editable;

	public StandloneWorkPageOfTB() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		content = new Composite(parent, SWT.NONE);
		content.setLayout(new GridLayout());
		taskForm = (TaskForm) input.getData();
		this.editable = input.isEditable();
		taskForm.addFieldValueListener("projecttemplate_id", this); //$NON-NLS-1$
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

		BasicDBList optionsValue = (BasicDBList) taskForm.getValue(fieldName);
		if (options != null) {
			for (int i = 0; i < options.size(); i++) {
				final Button settingButton = new Button(sectionClient,
						SWT.CHECK);
				final String optionName = (String) options.get(i);
				settingButton.setData("data", optionName); //$NON-NLS-1$
				settingButton.setText(optionName);
				settingButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						addFilterCondition(fieldName, optionName,
								settingButton.getSelection());
					}
				});
				settingButton.setSelection(optionsValue != null
						&& optionsValue.contains(optionName));

				settingButton.setEnabled(editable);
			}
		}

		section.setClient(sectionClient);
		return section;
	}

	protected void addFilterCondition(String optionFieldName, String option,
			boolean selection) {
		BasicDBList options = (BasicDBList) taskForm.getValue(optionFieldName);
		if (options == null) {
			options = new BasicDBList();
		}
		if (selection) {
			options.add(option);
		} else {
			options.remove(option);
		}
		taskForm.setValue(optionFieldName, options, this, true);
		setDirty(true);
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

	public void setDirty(boolean b) {
		dirty = b;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void commit(boolean onSave) {
		if (onSave) {
			setDirty(false);
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

	@Override
	public void valueChanged(String key, Object oldValue, Object newValue) {
		if (key.equals(Project.F_PROJECT_TEMPLATE_ID)
				&& Util.equals(oldValue, newValue)) {
			return;
		}
		createOptionContent();

	}

	private void createOptionContent() {
		Control[] children = content.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}

		Object projecttemplate_id = taskForm.getValue("projecttemplate_id"); //$NON-NLS-1$
		if(projecttemplate_id == null){
			return;
		}
		ProjectTemplate template = ModelService.createModelObject(
				ProjectTemplate.class,
				(ObjectId) projecttemplate_id);

		if (template == null) {
			return;
		}
		List<?> standardOptions = (List<?>) template
				.getValue(ProjectTemplate.F_STANDARD_OPTION_SET);
		createOptionSetting(content, standardOptions, Messages.get().StandloneWorkPageOfTB_0,
				ProjectTemplate.F_STANDARD_OPTION_SET);

		List<?> productTypeOptions = (List<?>) template
				.getValue(ProjectTemplate.F_PRODUCTTYPE_OPTION_SET);
		createOptionSetting(content, productTypeOptions, Messages.get().StandloneWorkPageOfTB_4,
				ProjectTemplate.F_PRODUCTTYPE_OPTION_SET);

		List<?> projectTypeOptions = (List<?>) template
				.getValue(ProjectTemplate.F_PROJECTTYPE_OPTION_SET);
		createOptionSetting(content, projectTypeOptions, Messages.get().StandloneWorkPageOfTB_5,
				ProjectTemplate.F_PROJECTTYPE_OPTION_SET);

		content.layout();
	}

	@Override
	public boolean checkValidOnSave() {
		// List<?> value = (List<?>) project
		// .getValue(ProjectTemplate.F_STANDARD_OPTION_SET);
		// if (value == null || value.size() == 0) {
		// return false;
		// }
		// value = (List<?>) project
		// .getValue(ProjectTemplate.F_PRODUCTTYPE_OPTION_SET);
		// if (value == null || value.size() == 0) {
		// return false;
		// }
		// value = (List<?>) project
		// .getValue(ProjectTemplate.F_PROJECTTYPE_OPTION_SET);
		// if (value == null || value.size() == 0) {
		// return false;
		// }
		return true;
	}

	@Override
	public boolean canRefresh() {
		return false;
	}
	@Override
	public boolean createBody() {
		return false;
	}
}
