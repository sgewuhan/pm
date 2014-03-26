package com.tmt.pdm.dcpdm.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class DocumentPage extends AbstractFormPageDelegator {

	@Override
	public void commit(boolean onSave) {
	}

	@Override
	public void setFocus() {
	}
	
	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		super.createPageContent(parent, input, conf);
		return new Composite(parent,SWT.NONE);
	}
	
	@Override
	public boolean createBody() {
		return true;
	}


}
