package com.sg.business.visualization.editor.projectset;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;

import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class ProjectSetDashboard extends AbstractFormPageDelegator {

	@Override
	public void commit(boolean onSave) {
	}

	@Override
	public void setFocus() {

	}

	@Override
	public Composite createPageContent(IManagedForm mForm, Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {

		return super.createPageContent(mForm, parent, input, conf);
	}

}
