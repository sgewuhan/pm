package com.tmt.gs.handler;

import org.eclipse.core.runtime.IProgressMonitor;

import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.commons.model.IEditorSaveHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class GSBusinessSupportAddParticipate implements IEditorSaveHandler {

	public GSBusinessSupportAddParticipate() {
	}

	@Override
	public boolean doSaveBefore(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		return true;
	}

	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		TaskForm taskform = (TaskForm) input.getData();
		Object value = taskform.getValue("support"); 

		if (value instanceof String) {
			Work work = taskform.getWork();
			work.addParticipate((String)value);
			try {
				work.doSave(new CurrentAccountContext());
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
		return true;
	}
}
