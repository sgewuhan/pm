package com.tmt.xt.handler;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.part.editor.IDataObjectDialogCallback;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class SupportNoticeAuditSave implements IDataObjectDialogCallback {

	public SupportNoticeAuditSave() {
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

	@Override
	public boolean doSaveBefore(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		return true;
	}

	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
        TaskForm taskform = (TaskForm) input.getData();
        Object listValue = taskform.getValue("supportnotice"); //$NON-NLS-1$
        if(listValue instanceof List){
        	List<?> userList=(List<?>)listValue;
        	Work work = taskform.getWork();
        	work.doAddParticipateList(userList);
        }
		return true;
	}

}
