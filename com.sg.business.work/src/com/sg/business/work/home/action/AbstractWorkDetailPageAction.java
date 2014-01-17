package com.sg.business.work.home.action;

import org.eclipse.swt.widgets.Control;

import com.sg.business.model.Work;
import com.sg.widgets.part.editor.IDataObjectEditorAction;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public abstract class AbstractWorkDetailPageAction implements IDataObjectEditorAction{
	@Override
	public boolean visiableWhen(PrimaryObjectEditorInput input) {
		return true;
	}

	@Override
	public boolean enableWhen(PrimaryObjectEditorInput input) {
		return true;
	}
	
	@Override
	public void run(PrimaryObjectEditorInput input, Control control) {
		run((Work)input.getData(),control);
	}

	protected abstract void run(Work data, Control control);
}
