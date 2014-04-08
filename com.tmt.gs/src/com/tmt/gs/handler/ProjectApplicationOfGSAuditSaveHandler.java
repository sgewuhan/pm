package com.tmt.gs.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.part.editor.IDataObjectDialogCallback;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class ProjectApplicationOfGSAuditSaveHandler implements
		IDataObjectDialogCallback {

	public ProjectApplicationOfGSAuditSaveHandler() {
	}

	@Override
	public boolean doSaveBefore(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		return true;
	}

	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		List<String> userList = new ArrayList<String>();
		TaskForm taskform = (TaskForm) input.getData();
		Object value = taskform.getValue("prj_manager"); //$NON-NLS-1$

		if (value instanceof String) {
			userList.add((String) value);
			Work work = taskform.getWork();
			work.doAddParticipateList(userList);;
		}

		return true;
	}

	@Override
	public boolean okPressed() {
		return false;
	}

	@Override
	public void cancelPressed() {

	}

	@Override
	public boolean needSave() {
		return true;
	}

}
