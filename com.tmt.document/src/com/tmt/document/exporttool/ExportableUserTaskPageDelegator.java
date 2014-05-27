package com.tmt.document.exporttool;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.IEditorPageLayoutProvider;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class ExportableUserTaskPageDelegator implements IPageDelegator {

	public ExportableUserTaskPageDelegator() {
	}

	@Override
	public Composite createPageContent(IManagedForm mForm,Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		IToolBarManager barManager = mForm.getForm().getToolBarManager();
		barManager.add(new ExportUserTaskAction(input));
		mForm.getForm().updateToolBar();
		return null;
	}

	@Override
	public IFormPart getFormPart() {
		return null;
	}

	@Override
	public boolean canRefresh() {
		return false;
	}

	@Override
	public void refresh() {

	}

	@Override
	public boolean createBody() {
		return true;
	}

	@Override
	public IEditorPageLayoutProvider getPageLayout() {
		return null;
	}

}
